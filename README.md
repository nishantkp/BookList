# Book Listing App 

Search your favourite books with google API.

#### Helpers :smiley:

- Goolge books API
- JSON
- Exception handeling
- AsynckTask
- Network call on background thread
- ViewHolder design pattern
- Shared preference
- Piccaso library

#### Features

- Search book with google books API
- Filter by autor or title or by specifing author and title
- Filter books by free, paid and both

#### Picasso library

- Handling ImageView recycling and download cancelation in an adapter.
- Complex image transformations with minimal memory use.
- Automatic memory and disk caching.

For more information click [here](http://square.github.io/picasso/).

###### Gradle dependencies
```gradle
compile 'com.squareup.picasso:picasso:2.5.2'
```
###### Adapter Download

```java
@Override public void getView(int position, View convertView, ViewGroup parent) {
  SquaredImageView view = (SquaredImageView) convertView;
  if (view == null) {
    view = new SquaredImageView(context);
  }
  String url = getItem(position);

  Picasso.with(context).load(url).into(view);
}
```

## UX Design

<img src="https://user-images.githubusercontent.com/32653955/34323519-20eb5a3e-e81b-11e7-82e2-9e160489aa11.png" width="275" height="475"> <img src="https://user-images.githubusercontent.com/32653955/34323527-69139bc8-e81b-11e7-89eb-507815b78b91.png" width="275" height="475"> <img src="https://user-images.githubusercontent.com/32653955/34323520-20f8b026-e81b-11e7-95d8-24d67e584ce2.png" width="275" height="475">
