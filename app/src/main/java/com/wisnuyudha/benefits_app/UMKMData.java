package com.wisnuyudha.benefits_app;

import java.util.ArrayList;

public class UMKMData {

    private static String [] namaUMKM = {
        "Contoh Pencarian 1",
        "Contoh Pencarian 2",
        "Contoh Pencarian 3",
        "Contoh Pencarian 4"
    };

    private static String [] deskripsiUMKM = {
        "Deskripsi Pencarian 1",
        "Deskripsi Pencarian 2",
        "Deskripsi Pencarian 3",
        "Deskripsi Pencarian 4"
    };

    private static int [] fotoUMKM = {
        R.drawable.ic_launcher_background,
        R.drawable.ic_launcher_background,
        R.drawable.ic_launcher_background,
        R.drawable.ic_launcher_background,
    };

    static ArrayList<UMKM> getListData(){
        ArrayList<UMKM> list = new ArrayList<>();
        for (int position = 0; position < namaUMKM.length; position++){
            UMKM umkm = new UMKM();
            umkm.setNamaUMKM(namaUMKM[position]);
            umkm.setDeskripsiUMKM(deskripsiUMKM[position]);
            umkm.setFotoUMKM(fotoUMKM[position]);
            list.add(umkm);
        }
        return list;
    }
}
