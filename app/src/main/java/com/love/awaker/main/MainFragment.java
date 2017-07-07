package com.love.awaker.main;

import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.love.awaker.R;
import com.love.awaker.base.BaseObservableList;
import com.love.awaker.base.listener.OnItemClickListener;
import com.love.awaker.data.New;
import com.love.awaker.databinding.FragMainBinding;

/**
 * Created by ruzhan on 2017/7/6.
 */

public class MainFragment extends Fragment implements OnItemClickListener<New> {

    private FragMainBinding binding;
    private MainViewModel mainViewModel;
    private MainAdapter adapter;

    private NewListCallBack newListCallBack;
    private EmptyCallBack emptyCallBack;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.frag_main, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new MainAdapter(this);
        binding.recyclerView.setAdapter(adapter);
        mainViewModel.getNewList("f32b30c2a289bfca2c9857ffc5871ac8", 1, 0);

        newListCallBack = new NewListCallBack();
        mainViewModel.news.addOnListChangedCallback(newListCallBack);

        emptyCallBack = new EmptyCallBack();
        mainViewModel.isEmpty.addOnPropertyChangedCallback(emptyCallBack);
    }

    @Override
    public void onDestroyView() {
        mainViewModel.news.removeOnListChangedCallback(newListCallBack);
        mainViewModel.isEmpty.removeOnPropertyChangedCallback(emptyCallBack);

        super.onDestroyView();
    }

    public void setMainViewModel(MainViewModel mainViewModel) {
        this.mainViewModel = mainViewModel;
    }

    @Override
    public void onItemClick(View view, int position, New bean) {
        Toast.makeText(getContext(), position + " : " + bean.title, Toast.LENGTH_SHORT).show();
    }

    private class NewListCallBack extends BaseObservableList<New> {

        @Override
        public void onChangedList(ObservableList<New> sender) {
            adapter.setData(sender);
            Toast.makeText(getActivity(), "onChangedList", Toast.LENGTH_SHORT).show();
        }
    }

    private class EmptyCallBack extends Observable.OnPropertyChangedCallback {

        @Override
        public void onPropertyChanged(Observable sender, int propertyId) {
            boolean isEmpty = ((ObservableBoolean) sender).get();
            binding.setIsEmpty(isEmpty);
        }
    }
}
