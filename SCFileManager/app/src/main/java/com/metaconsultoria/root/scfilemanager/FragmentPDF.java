package com.metaconsultoria.root.scfilemanager;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnDrawListener;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageScrollListener;

public class FragmentPDF extends Fragment implements OnPageChangeListener,  OnLoadCompleteListener, OnDrawListener, OnErrorListener, OnPageScrollListener{
    private SharedPreferences pdfReader;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fraglayoutpdf,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        String caminho = bundle.getString("caminho");
        pdfReader = getContext().getSharedPreferences("PDFReader", Context.MODE_PRIVATE);
        PDFView pdfviewer = view.findViewById(R.id.pdfviewer);
        pdfviewer.fromUri(Uri.parse(caminho))
                .defaultPage(0)
                .onPageChange(this)
                .swipeHorizontal(false)
                .enableAnnotationRendering(true)
                .scrollHandle(null)
                .onLoad(this)
                .onDraw(this)
                .enableSwipe(true)
                .onError(this)
                .enableDoubletap(true)
                .onPageScroll(this)
                .scrollHandle(null)
                .spacing(20)
                .load();
    }
    @Override
    public void onPageChanged(int page, int pageCount) {
        SharedPreferences.Editor edit = pdfReader.edit();
        edit.putInt("pages",page);
        edit.commit();
    }

    @Override
    public void loadComplete(int nbPages) {

    }

    @Override
    public void onLayerDrawn(Canvas canvas, float pageWidth, float pageHeight, int displayedPage) {

    }

    @Override
    public void onError(Throwable t) {

    }

    @Override
    public void onPageScrolled(int page, float positionOffset) {
    }
}
