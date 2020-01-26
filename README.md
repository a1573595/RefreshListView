# RefreshListView
ListView can be drop-down refreshing and slide-up loading, support refresh &amp; load failed listen.


## Gradle
```
dependencies {
	        implementation 'com.github.a1573595:RefreshListView:Tag'
}
```

## Usage
```
<com.a1573595.refreshlistview.RefreshListView
        android:id="@+id/refreshListView"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />
```

```
refreshListView.setRefreshEnable(false);
refreshListView.setLoadMoreEnable(false);
refreshListView.setUpdateListener(this);
refreshListView.setFailedListener(this);
refreshListView.setResetTime(3000L);
refreshListView.setListViewPadding(0, 0, 0, 24);
        
refreshListView.getListView().setAdapter(arrayAdapter);
```
