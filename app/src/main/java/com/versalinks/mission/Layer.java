package com.versalinks.mission;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Layer {
    public List<Item> items;
    public String label;

    public static class Item {
        public int resID;
        public String label;

        public Item(String label, int resID) {
            this.label = label;
            this.resID = resID;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Item item = (Item) o;
            return label.equals(item.label);
        }

        @Override
        public int hashCode() {
            return Objects.hash(label);
        }
    }

    public static Layer create1() {
        Layer layer = new Layer();
        layer.label = "生态体验点";
        layer.items = new ArrayList<>();
        layer.items.add(new Item("自然科普", R.drawable.ic_location));
        layer.items.add(new Item("观光旅游", R.drawable.ic_location));
        layer.items.add(new Item("户外体验", R.drawable.ic_location));
        layer.items.add(new Item("专项旅游", R.drawable.ic_location));
        return layer;
    }

    public static Layer create2() {
        Layer layer = new Layer();
        layer.label = "珍稀动植物";
        layer.items = new ArrayList<>();
        layer.items.add(new Item("动物", R.drawable.ic_location));
        layer.items.add(new Item("植物", R.drawable.ic_location));
        return layer;
    }

    public static Layer create3() {
        Layer layer = new Layer();
        layer.label = "基础图层";
        layer.items = new ArrayList<>();
        layer.items.add(new Item("水系", R.drawable.ic_location));
        layer.items.add(new Item("村庄", R.drawable.ic_location));
        layer.items.add(new Item("山峰", R.drawable.ic_location));
        layer.items.add(new Item("道路", R.drawable.ic_location));
        return layer;
    }
}
