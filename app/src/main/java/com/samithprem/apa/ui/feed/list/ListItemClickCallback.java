package com.samithprem.apa.ui.feed.list;

import com.samithprem.apa.data.source.remote.api.model.Feed;

public interface ListItemClickCallback {
    void onClick(Feed feed);
}