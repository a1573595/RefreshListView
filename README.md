# RefreshListView
ListView can be drop-down refreshing and slide-up loading, support refresh &amp; load failed listen.

![RefreshListView](https://user-images.githubusercontent.com/25738593/73148929-bf601180-40f9-11ea-8868-9cf62cbcb0ab.gif)

## Gradle
```
android {
    ...
    
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation 'com.github.a1573595:RefreshListView:1.1.0'
}
```

## Usage
```
<com.a1573595.refreshlistview.RefreshListView
        android:id="@+id/refreshListView"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />
```
