package com.little.framework.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;

/**
 * User: juqiu.lt@alibaba
 * Date: 2015-07-16
 * Time: 10:31
 *  Add header and footer view
 */
public class ExtendedRecyclerView extends RecyclerView {

    public static final int ITEM_VIEW_TYPE_MASK = 0x8000;

    public static final int ITEM_POSITION_MASK = 0x7fff;

    private ArrayList<View> mHeaderViews = new ArrayList<View>();

    private ArrayList<View> mFooterViews = new ArrayList<View>();

    private View mEmtyView;

    private RecyclerView.Adapter mOriginAdapter;

    private int mContentTopClearance = 0;
    
    public ExtendedRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public ExtendedRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExtendedRecyclerView(Context context) {
        this(context, null);
    }


    public static class HeaderViewListAdapter extends Adapter implements Filterable {

        private final Adapter mAdapter;

        private final RecyclerView mRecyclerView;

        ArrayList<View> mHeaderViews;

        ArrayList<View> mFooterViews;

        static final ArrayList<View> EMPTY_INFO_LIST = new ArrayList<View>();

        private final boolean mIsFilterable;

        public HeaderViewListAdapter(ArrayList<View> headerViews, ArrayList<View> footerViews, Adapter adapter, RecyclerView recyclerView) {
            mAdapter = adapter;
            mRecyclerView = recyclerView;
            mIsFilterable = adapter instanceof Filterable;

            if (headerViews == null) {
                mHeaderViews = EMPTY_INFO_LIST;
            } else {
                mHeaderViews = headerViews;
            }

            if (footerViews == null) {
                mFooterViews = EMPTY_INFO_LIST;
            } else {
                mFooterViews = footerViews;
            }
        }

        public int getHeadersCount() {
            return mHeaderViews.size();
        }

        public int getFootersCount() {
            return mFooterViews.size();
        }

        public boolean isEmpty() {
            return mAdapter == null || 0 == mAdapter.getItemCount();
        }

        public boolean removeHeader(View v) {
            for (int i = 0; i < mHeaderViews.size(); i++) {
                View view = mHeaderViews.get(i);
                if (view == v) {
                    mHeaderViews.remove(i);
                    return true;
                }
            }

            return false;
        }

        public boolean removeFooter(View v) {
            for (int i = 0; i < mFooterViews.size(); i++) {
                View view = mFooterViews.get(i);
                if (view == v) {
                    mFooterViews.remove(i);
                    return true;
                }
            }
            return false;
        }

        @Override
        public long getItemId(int position) {
            int numHeaders = getHeadersCount();
            if (mAdapter != null && position >= numHeaders) {
                int adjPosition = position - numHeaders;
                int adapterCount = mAdapter.getItemCount();
                if (adjPosition < adapterCount) {
                    return mAdapter.getItemId(adjPosition);
                }
            }
            return NO_ID;
        }

        @Override
        public int getItemCount() {
            if(mRecyclerView instanceof  ExtendedRecyclerView){
                ExtendedRecyclerView extendedRecyclerView= (ExtendedRecyclerView) mRecyclerView;
                boolean isShowEmptyView =extendedRecyclerView.getCount() == 0 ?true : false;
                if (extendedRecyclerView.mEmtyView != null)
                    extendedRecyclerView.mEmtyView.setVisibility(isShowEmptyView ? View.VISIBLE : View.GONE);
                    if(isShowEmptyView){
                        return  0;
                    }
            }

            if (mAdapter != null) {
                return getFootersCount() + getHeadersCount() + mAdapter.getItemCount();
            } else {
                return getFootersCount() + getHeadersCount();
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (0 == (viewType & ITEM_VIEW_TYPE_MASK)) {
                if (mAdapter != null) {
                    return mAdapter.onCreateViewHolder(parent, viewType);
                }
            } else {
                int position = viewType & ITEM_POSITION_MASK;
                int numHeaders = getHeadersCount();
                if (position < numHeaders) {
                    return new HeaderViewHolder(mHeaderViews.get(position));
                } else {
                    final int adjPosition = position - numHeaders;
                    int adapterCount = 0;
                    if (mAdapter != null) {
                        adapterCount = mAdapter.getItemCount();
                    }
                    return new HeaderViewHolder(mFooterViews.get(adjPosition - adapterCount));
                }
            }

            return null;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            int numHeaders = getHeadersCount();
            int numFooters = getFootersCount();
            if (position < numHeaders || position >= getItemCount() - numFooters) {
                ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
                if (null == lp) {
                    lp = mRecyclerView.getLayoutManager().generateDefaultLayoutParams();
                    holder.itemView.setLayoutParams(lp);
                }

                if (lp instanceof StaggeredGridLayoutManager.LayoutParams) {
                    ((StaggeredGridLayoutManager.LayoutParams) lp).setFullSpan(true);
                } else if (lp instanceof RecyclerView.LayoutParams) {
                    lp.width = LayoutParams.MATCH_PARENT;
                    lp.height = LayoutParams.MATCH_PARENT;
                }
            } else {
                final int adjPosition = position - numHeaders;
                if (mAdapter != null) {
                    mAdapter.onBindViewHolder(holder, adjPosition);
                }
            }
        }

        @Override
        public int getItemViewType(int position) {
            int numHeaders = getHeadersCount();
            if (mAdapter != null && position >= numHeaders) {
                int adjPosition = position - numHeaders;
                int adapterCount = mAdapter.getItemCount();
                if (adjPosition < adapterCount) {
                    return mAdapter.getItemViewType(adjPosition);
                }
            }

            return position | ITEM_VIEW_TYPE_MASK;
        }

        @Override
        public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
            if (mAdapter != null) {
                mAdapter.registerAdapterDataObserver(observer);
            }
        }

        @Override
        public void unregisterAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
            if (mAdapter != null) {
                mAdapter.unregisterAdapterDataObserver(observer);
            }
        }

        public Filter getFilter() {
            if (mIsFilterable) {
                return ((Filterable) mAdapter).getFilter();
            }
            return null;
        }

        public Adapter getWrappedAdapter() {
            return mAdapter;
        }
    }

    /**
     *
     * @param v
     */
    public final void addHeaderView(View v) {
        if (v == null){
            return;
        }
        mHeaderViews.add(v);

        final Adapter adapter = getAdapter();
        if (adapter != null) {
            if (!(adapter instanceof HeaderViewListAdapter)) {
                setAdapter(new HeaderViewListAdapter(mHeaderViews, mFooterViews, adapter, this));
            } else {
                adapter.notifyDataSetChanged();
            }
        }

        final LayoutManager lm = getLayoutManager();
        if (lm != null) {
            setLayoutManager(lm);
        }
    }

    public final void addFooterView(View v) {
        if (v == null){
            return;
        }
        mFooterViews.add(v);

        final Adapter adapter = getAdapter();
        if (adapter != null) {
            if (!(adapter instanceof HeaderViewListAdapter)) {
                setAdapter(new HeaderViewListAdapter(mHeaderViews, mFooterViews, adapter, this));
            } else {
                adapter.notifyDataSetChanged();
            }
        }

        final LayoutManager lm = getLayoutManager();
        if (lm != null) {
            setLayoutManager(lm);
        }
    }

    public final boolean removeHeaderView(View v) {
        if (mHeaderViews.size() > 0) {
            boolean result = false;
            final Adapter adapter = getAdapter();
            if (adapter != null && ((HeaderViewListAdapter) adapter).removeHeader(v)) {
                adapter.notifyDataSetChanged();
                result = true;
            }
            removeFixedViewInfo(v, mHeaderViews);
            return result;
        }
        return false;
    }

    public final boolean removeFooterView(View v) {
        if (mFooterViews.size() > 0) {
            boolean result = false;
            final Adapter adapter = getAdapter();
            if (adapter != null && ((HeaderViewListAdapter) adapter).removeHeader(v)) {
                adapter.notifyDataSetChanged();
                result = true;
            }
            removeFixedViewInfo(v, mFooterViews);
            return result;
        }
        return false;
    }

    private void removeFixedViewInfo(View v, ArrayList<View> where) {
        int len = where.size();
        for (int i = 0; i < len; ++i) {
            View view = where.get(i);
            if (view == v) {
                where.remove(i);
                break;
            }
        }
    }

    public int getHeaderViewsCount() {
        return mHeaderViews.size();
    }

    public int getFooterViewsCount() {
        return mFooterViews.size();
    }

    public int getItemCount() {

        return getTotalCount() - getHeaderViewsCount() - getFooterViewsCount();
    }

    public int getTotalCount() {
        Adapter adapter = getAdapter();
        if (null == adapter) {
            return 0;
        } else {
            return adapter.getItemCount();
        }
    }

    @Override
    public void setAdapter(Adapter adapter) {
        this.mOriginAdapter =adapter;
        if (null != adapter && !(adapter instanceof HeaderViewListAdapter) && (mHeaderViews.size() > 0 || mFooterViews.size() > 0)) {
            super.setAdapter(new HeaderViewListAdapter(mHeaderViews, mFooterViews, adapter, this));
        } else {
            super.setAdapter(adapter);
        }
    }

    @Override
    public void swapAdapter(Adapter adapter, boolean removeAndRecycleExistingViews) {
        this.mOriginAdapter =adapter;
        if (null != adapter && !(adapter instanceof HeaderViewListAdapter)) {
            if (mHeaderViews.size() > 0 || mFooterViews.size() > 0) {
                super.swapAdapter(new HeaderViewListAdapter(mHeaderViews, mFooterViews, adapter, this), removeAndRecycleExistingViews);
                return;
            }
        }
        super.swapAdapter(adapter, removeAndRecycleExistingViews);
    }


    public static class HeaderViewHolder extends RecyclerView.ViewHolder {

        public HeaderViewHolder(View view) {
            super(view);
        }
    }

    public void setEmptyView(View emptyView) {
        this.mEmtyView = emptyView;
        emptyView.setVisibility(getCount() == 0 ? View.VISIBLE : View.GONE);
    }

    private int getCount() {
        if(mOriginAdapter !=null){
            return  mOriginAdapter.getItemCount();
        }
        return 0;
    }
}
