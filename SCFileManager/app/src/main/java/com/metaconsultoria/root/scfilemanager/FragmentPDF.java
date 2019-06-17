package com.metaconsultoria.root.scfilemanager;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;

public class FragmentPDF extends Fragment {
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
        PDFView pdfviewer = view.findViewById(R.id.pdfviewer);

        //Toast.makeText(getActivity().getApplicationContext(),caminho,Toast.LENGTH_LONG).show();

        pdfviewer.fromUri(Uri.parse(caminho)).load();
    }
}
