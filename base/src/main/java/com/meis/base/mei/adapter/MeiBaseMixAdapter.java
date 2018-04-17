package com.meis.base.mei.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseViewHolder;
import com.meis.base.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * 多种类型混合的适配器
 */
public class MeiBaseMixAdapter extends MeiBaseAdapter<Object> {

    private SparseArray<ItemPresenter> mItemPresenters = new SparseArray<>();

    /**
     * layouts indexed with their types
     */
    private SparseArray<Integer> layouts;

    private static final int DEFAULT_VIEW_TYPE = -0xff;

    private SparseArray mUniqueData = new SparseArray<>();

    /**
     * 是否过滤重复数据
     */
    private boolean mFilterUnique = true;

    public MeiBaseMixAdapter(List data, ItemPresenter... itemPresenters) {
        super(data);
        setHasStableIds(true);
        for (ItemPresenter itemPresenter : itemPresenters) {
            addItemPresenter(itemPresenter);
        }
    }

    public MeiBaseMixAdapter(ItemPresenter... itemPresenters) {
        super();
        setHasStableIds(true);
        for (ItemPresenter itemPresenter : itemPresenters) {
            addItemPresenter(itemPresenter);
        }
    }

    public void setFilterUnique(boolean filterUnique) {
        mFilterUnique = filterUnique;
    }

    @Override
    public void setNewData(@Nullable List data) {
        if (mFilterUnique) {
            clearUniqueData();
            filterUnique(data);
        }
        super.setNewData(data);
    }

    @Override
    public void addData(@NonNull Collection<?> newData) {
        if (mFilterUnique) {
            List updates = filterUnique((List) newData);
            if (updates != null && updates.size() > 0) {
                int position = indexOfItem(updates.get(0));
                if (position >= 0) {
                    notifyItemRangeChanged(position, updates.size());
                }
            }
        }
        super.addData(newData);
    }

    @Override
    protected BaseViewHolder createBaseViewHolder(View view) {
        return new BaseViewHolder(view);
    }

    @Override
    public long getItemId(int position) {
        if (position != -1 && position < mData.size())
            return getItem(position).hashCode();
        else {
            return -1;
        }

    }

    public void addItemPresenter(ItemPresenter itemPresenter) {
        if (itemPresenter instanceof MultiItemPresenter) {
            int[] itemTypes = ((MultiItemPresenter) itemPresenter).getItemTypes();
            mItemPresenters.put(itemPresenter.getItemType(), itemPresenter);
            for (int itemType : itemTypes) {
                addLayoutType(itemType, ((MultiItemPresenter) itemPresenter).getLayoutRes
                        (itemType));
            }
        } else {
            int itemType = itemPresenter.getItemType();
            mItemPresenters.put(itemType, itemPresenter);
            addLayoutType(itemType, itemPresenter.getLayoutRes());
        }
    }

    @Override
    protected void convert(BaseViewHolder helper, Object item) {
        ItemPresenter itemPresenter = mItemPresenters.get(item.getClass().hashCode());
        if (itemPresenter != null) {
            try {
                itemPresenter.convert(helper, item);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected int getDefItemViewType(int position) {
        Object item = mData.get(position);
        ItemPresenter itemPresenter = mItemPresenters.get(item.getClass().hashCode());
        if (itemPresenter != null) {
            if (itemPresenter instanceof MultiItemPresenter) {
                return ((MultiItemPresenter) itemPresenter).getItemType(item);
            } else {
                return itemPresenter.getItemType();
            }
        }
        return DEFAULT_VIEW_TYPE;
    }

    protected void setDefaultViewTypeLayout(@LayoutRes int layoutResId) {
        addLayoutType(DEFAULT_VIEW_TYPE, layoutResId);
    }

    @Override
    protected BaseViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
        return createBaseViewHolder(parent, getLayoutId(viewType));
    }

    private int getLayoutId(int viewType) {
        return layouts.get(viewType) == null ? R.layout.mei_empty_item : layouts.get(viewType);
    }

    private void addLayoutType(int type, @LayoutRes int layoutResId) {
        if (layouts == null) {
            layouts = new SparseArray<>();
        }
        layouts.put(type, layoutResId);
    }

    public void removeData(Object o) {
        int indexOf = getData().indexOf(o);
        if (indexOf != -1) {
            remove(indexOf);
        }
    }

    public void removeData(int position) {
        if (position >= 0 && position < getData().size()) {
            remove(position);
        }
    }

    public void clearDatas() {
        getData().clear();
        notifyDataSetChanged();
    }

    @Override
    public void onViewDetachedFromWindow(BaseViewHolder holder) {
        ItemPresenter itemPresenter = mItemPresenters.get(holder.getItemViewType());
        if (itemPresenter != null) {
            itemPresenter.onViewDetached(holder);
        }
        super.onViewRecycled(holder);
    }

    public int indexOfItem(Object item) {
        return getData().indexOf(item);
    }

    private void clearUniqueData() {
        mUniqueData.clear();
    }

    private List filterUnique(@Nullable List newData) {
        if (newData != null && newData.size() > 0) {
            List updates = null;
            for (Iterator iterator = newData.iterator(); iterator.hasNext(); ) {
                Object t = iterator.next();
                if (t != null) {
                    int hash = t.hashCode();
                    Object oldT = mUniqueData.get(hash);
                    if (oldT != null) {
                        if (oldT instanceof IUpdatable && oldT.getClass() == t.getClass()) {
                            if (updates == null) {
                                updates = new ArrayList<>();
                            }
                            ((IUpdatable) oldT).update(t);
                            updates.add(oldT);
                        }
                        iterator.remove();
                    } else {
                        mUniqueData.put(hash, t);
                    }
                }
            }
            return updates;
        }
        return null;
    }
}
