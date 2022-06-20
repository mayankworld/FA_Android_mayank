package com.firstapp.fa_mayanksanjeevnibhoria_c0854281_android;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.hudomju.swipe.SwipeToDismissTouchListener;
import com.hudomju.swipe.adapter.ListViewAdapter;

import java.util.List;

public class ListFragment extends Fragment {

    ListView listView;
    DbOperations dbOperations;
    List<BeanPlaces> list;
    ListPlacesAdapter la;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        listView=view.findViewById(R.id.list);

        dbOperations=new DbOperations(getActivity());

        list=dbOperations.getAllPlaces();

        Log.e("list", ""+list);

        la=new ListPlacesAdapter(getActivity(), list);
        listView.setAdapter(la);

        SwipeToDismissTouchListener<ListViewAdapter> touchListener =
                new SwipeToDismissTouchListener<>(
                        new ListViewAdapter(listView),
                        new SwipeToDismissTouchListener.DismissCallbacks<ListViewAdapter>() {
                            @Override
                            public boolean canDismiss(int position) {
                                return true;
                            }

                            @Override
                            public void onDismiss(ListViewAdapter view, int position) {

                                dbOperations.delete(list.get(position).getId());

                                la.remove(position);
                            }
                        });

        listView.setOnTouchListener(touchListener);
        listView.setOnScrollListener((AbsListView.OnScrollListener) touchListener.makeScrollListener());
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (touchListener.existPendingDismisses()) {

                    touchListener.undoPendingDismiss();

                    BeanPlaces b = list.get(position);

                    Intent intent = new Intent(getActivity(), EditPlace.class);
                    intent.putExtra("id", String.valueOf(b.getId()));
                    intent.putExtra("pname", b.getPname());
                    intent.putExtra("paddr", b.getPaddr());
                    intent.putExtra("plat", b.getPlat());
                    intent.putExtra("plng", b.getPlng());
                    intent.putExtra("pv", b.getPv());
                    intent.putExtra("pdate", b.getPdate());
                    startActivity(intent);

                } else {
                    BeanPlaces b = list.get(position);

                    Intent intent = new Intent(getActivity(), MapsActivity.class);
                    intent.putExtra("id", String.valueOf(b.getId()));
                    intent.putExtra("pname", b.getPname());
                    intent.putExtra("paddr", b.getPaddr());
                    intent.putExtra("plat", b.getPlat());
                    intent.putExtra("plng", b.getPlng());
                    intent.putExtra("pv", b.getPv());
                    intent.putExtra("pdate", b.getPdate());

                    startActivity(intent);
                }
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        list=dbOperations.getAllPlaces();
        la=new ListPlacesAdapter(getActivity(), list);
        listView.setAdapter(la);

        SwipeToDismissTouchListener<ListViewAdapter> touchListener =
                new SwipeToDismissTouchListener<>(
                        new ListViewAdapter(listView),
                        new SwipeToDismissTouchListener.DismissCallbacks<ListViewAdapter>() {
                            @Override
                            public boolean canDismiss(int position) {
                                return true;
                            }

                            @Override
                            public void onDismiss(ListViewAdapter view, int position) {

                                dbOperations.delete(list.get(position).getId());

                                la.remove(position);
                            }
                        });

        listView.setOnTouchListener(touchListener);
        listView.setOnScrollListener((AbsListView.OnScrollListener) touchListener.makeScrollListener());
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (touchListener.existPendingDismisses()) {

                    touchListener.undoPendingDismiss();

                    BeanPlaces b = list.get(position);

                    Intent intent = new Intent(getActivity(), EditPlace.class);
                    intent.putExtra("id", String.valueOf(b.getId()));
                    intent.putExtra("pname", b.getPname());
                    intent.putExtra("paddr", b.getPaddr());
                    intent.putExtra("plat", b.getPlat());
                    intent.putExtra("plng", b.getPlng());
                    intent.putExtra("pv", b.getPv());
                    intent.putExtra("pdate", b.getPdate());
                    startActivity(intent);

                } else {
                    BeanPlaces b = list.get(position);

                    Intent intent = new Intent(getActivity(), MapsActivity.class);
                    intent.putExtra("id", String.valueOf(b.getId()));
                    intent.putExtra("pname", b.getPname());
                    intent.putExtra("paddr", b.getPaddr());
                    intent.putExtra("plat", b.getPlat());
                    intent.putExtra("plng", b.getPlng());
                    intent.putExtra("pv", b.getPv());
                    intent.putExtra("pdate", b.getPdate());

                    startActivity(intent);
                }
            }
        });

    }
}