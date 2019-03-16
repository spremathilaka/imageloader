package com.samithprem.apa.ui.feed.list.adapter;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.samithprem.apa.R;
import com.samithprem.apa.data.source.remote.api.model.Feed;
import com.samithprem.apa.databinding.FeedListItemBinding;
import com.samithprem.apa.ui.feed.list.ListItemClickCallback;

import java.util.List;
import java.util.Objects;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.FeedViewHolder> {

    private List<Feed> feedList;

    @Nullable
    private final ListItemClickCallback itemClickCallback;

    public FeedAdapter(@Nullable ListItemClickCallback itemClickCallback) {
        this.itemClickCallback = itemClickCallback;
    }

    public void setFeedList(final List<Feed> newFeeds) {
        if (this.feedList == null) {
            this.feedList = newFeeds;
            notifyItemRangeInserted(0, feedList.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return FeedAdapter.this.feedList.size();
                }

                @Override
                public int getNewListSize() {
                    return newFeeds.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return FeedAdapter.this.feedList.get(oldItemPosition).getTitle().equals(
                            feedList.get(newItemPosition).getTitle());
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    Feed feed = feedList.get(newItemPosition);
                    Feed old = feedList.get(oldItemPosition);
                    return feed.getTitle() == old.getTitle()
                            && Objects.equals(feed.getDescription(), old.getDescription())
                            && Objects.equals(feed.getImageHref(), old.getImageHref()
                    );
                }
            });
            this.feedList = newFeeds;
            result.dispatchUpdatesTo(this);
        }
    }

    @NonNull
    @Override
    public FeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        FeedListItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.feed_list_item,
                        parent, false);

        binding.setCallback(itemClickCallback);

        return new FeedViewHolder(binding);
    }

    // Clean all elements of the recycler
    public void clear() {
        if (feedList != null) {
            feedList.clear();
            notifyDataSetChanged();
        }
    }

    // Add a list of items -- change to type used
    public void addAll(List<Feed> list) {
        feedList.addAll(list);
        notifyDataSetChanged();
    }


    @Override
    public void onBindViewHolder(FeedViewHolder holder, int position) {
        holder.binding.setFeed(feedList.get(position));
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return feedList == null ? 0 : feedList.size();
    }

    static class FeedViewHolder extends RecyclerView.ViewHolder {

        final FeedListItemBinding binding;

        FeedViewHolder(FeedListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}