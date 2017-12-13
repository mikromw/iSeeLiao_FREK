package com.milfrost.frek.modul.dashboard.emergency_number;

import android.content.Context;

import com.milfrost.frek.models.EmergencyNumber;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ASUS on 05/12/2017.
 */

public class EmergencyNumberPresenter {

    Context context;
    EmergencyNumberInterface.View viewInterface;

    public EmergencyNumberPresenter (Context context){
        this.context = context;
    }

    public void loadData(){
        List<EmergencyNumber> emergencyNumbers = new ArrayList<>();
        emergencyNumbers.add(new EmergencyNumber("RS. Methodist","(061) 7369000",EmergencyNumber.DOCTOR));
        emergencyNumbers.add(new EmergencyNumber("RS. Columbia Asia","(061) 4566368",EmergencyNumber.DOCTOR));
        emergencyNumbers.add(new EmergencyNumber("Kantor SAR Medan","(061) 8368111",EmergencyNumber.SAVE_AND_RESCUE));
        emergencyNumbers.add(new EmergencyNumber("Polsek Medan Area","(061) 4556732",EmergencyNumber.POLICE));
        emergencyNumbers.add(new EmergencyNumber("RS. Deli","(061) 4565229",EmergencyNumber.DOCTOR));
        emergencyNumbers.add(new EmergencyNumber("Polsek Medan Baru","(061) 4523141",EmergencyNumber.POLICE));
        emergencyNumbers.add(new EmergencyNumber("Pemadam Kebakaran","(061) 4515356",EmergencyNumber.FIREFIGHTER));

        if(viewInterface!=null){
            viewInterface.setList(emergencyNumbers);
            viewInterface.notifyAdapter();
        }
    }

    public void filterSearch(){

    }

    public void getCurrentLocation(){

    }


}
