package android.view;public class ViewGroup extends View{public View[] children=new View[0];public int getChildCount(){return children.length;}public View getChildAt(int i){return children[i];}}
