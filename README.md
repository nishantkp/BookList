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

## LICENSE INFO

```
Copyright (c) 2017 Nishant Patel

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
