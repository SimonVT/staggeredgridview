package net.simonvt.staggeredgridview.samples;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.simonvt.staggeredgridview.StaggeredGridView;

public class GridSample extends Activity {

  private List<Item> items = new ArrayList<Item>();

  private MyAdapter adapter;

  StaggeredGridView grid;

  long id;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.content);

    View emptyView = findViewById(android.R.id.empty);
    grid = (StaggeredGridView) findViewById(android.R.id.list);
    grid.setEmptyView(emptyView);
    grid.setItemMargin(10);
    grid.setOnItemClickListener(new StaggeredGridView.OnItemClickListener() {
      @Override
      public void onItemClick(StaggeredGridView parent, View view, int position, long id) {
        Log.d("GridSample", "Position: " + position);
      }
    });

    findViewById(R.id.remove_start).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (items.size() > 0) {
          items.remove(0);
          adapter.notifyDataSetChanged();
        }
      }
    });

    findViewById(R.id.remove_end).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (items.size() > 0) {
          items.remove(items.size() - 1);
          adapter.notifyDataSetChanged();
        }
      }
    });

    findViewById(R.id.add_header).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        items.add(new Header(++id, "Header " + id, getRandomColor()));
        adapter.notifyDataSetChanged();
      }
    });
    findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        items.add(createItem(id++));
        adapter.notifyDataSetChanged();
      }
    });

    items.add(new Header(id++, "Header 1", getRandomColor()));
    for (long end = id + 10; id <= end; id++) {
      items.add(createItem(id));
    }

    adapter = new MyAdapter(items);
    grid.setAdapter(adapter);
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    menu.add(0, 1, 0, "Add column");
    menu.add(0, 2, 0, "Remove column");

    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case 1:
        grid.setColumnCount(grid.getColumnCount() + 1);
        return true;

      case 2:
        final int columnCount = grid.getColumnCount();
        grid.setColumnCount(Math.max(columnCount - 1, 1));
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  private int getRandomColor() {
    Random random = new Random();
    int r = (int) (random.nextFloat() * 255);
    int g = (int) (random.nextFloat() * 255);
    int b = (int) (random.nextFloat() * 255);

    return Color.rgb(r, g, b);
  }

  private Item createItem(long id) {
    Random random = new Random();
    final float rand = random.nextFloat();
    final int count = (int) Math.ceil(rand * 15);
    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < count; i++) {
      if (i > 0) {
        builder.append("\n");
      }
      builder.append("Item ").append(id);
    }

    return new Item(id, builder.toString(), getRandomColor());
  }

  private static class Item {
    long id;

    String title;

    int color;

    private Item(long id, String title, int color) {
      this.id = id;
      this.title = title;
      this.color = color;
    }
  }

  private static class Header extends Item {

    private Header(long id, String title, int color) {
      super(id, title, color);
    }
  }

  private class MyAdapter extends BaseAdapter {

    private List<Item> items;

    private MyAdapter(List<Item> items) {
      this.items = items;
    }

    @Override public int getCount() {
      return items.size();
    }

    @Override public Item getItem(int position) {
      return items.get(position);
    }

    @Override public long getItemId(int position) {
      return items.get(position).id;
    }

    @Override public int getViewTypeCount() {
      return 2;
    }

    @Override public int getItemViewType(int position) {
      return getItem(position) instanceof Header ? 0 : 1;
    }

    @Override public boolean areAllItemsEnabled() {
      return false;
    }

    @Override public boolean isEnabled(int position) {
      return !(getItem(position) instanceof Header);
    }

    @Override public View getView(int position, View convertView, ViewGroup parent) {
      Item item = getItem(position);
      TextView v = (TextView) convertView;

      if (item instanceof Header) {
        if (v == null) {
          v = (TextView) LayoutInflater.from(GridSample.this)
              .inflate(R.layout.row_header, parent, false);
        }

        v.setText(item.title);
      } else {
        if (v == null) {
          v = (TextView) LayoutInflater.from(GridSample.this)
              .inflate(R.layout.row_item, parent, false);
        }

        v.setText(item.title);
      }

      v.setBackgroundColor(item.color);

      return v;
    }
  }
}
