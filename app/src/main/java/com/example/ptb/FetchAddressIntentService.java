package com.example.ptb;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static com.example.ptb.Contstants.FAILURE_RESULT;
import static com.example.ptb.Contstants.LOCATION_DATA_EXTRA;
import static com.example.ptb.Contstants.RECEIVER;
import static com.example.ptb.Contstants.RESULT_DATA_KEY;
import static com.example.ptb.Contstants.SUCCESS_RESULT;

public class FetchAddressIntentService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */

    private ResultReceiver resultReceiver;
    public FetchAddressIntentService() {
        super("FetchAddressIntentService");
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(intent!=null){
            String errorMessage = "";
            resultReceiver = intent.getParcelableExtra(RECEIVER);
            Location location = intent.getParcelableExtra(LOCATION_DATA_EXTRA);
            if(location==null){
                return;
            }
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = null;
            try {
                addresses=geocoder.getFromLocation(location.getLatitude(), location.getLongitude(),1);
            } catch (Exception e) {
                errorMessage = e.getMessage();
            }
            if(addresses==null || addresses.isEmpty()){
                deliverResultToReceiver(FAILURE_RESULT, errorMessage);
            }else{
                Address address = addresses.get(0);
                ArrayList<String> addressFragment = new ArrayList<>();
                for (int i =0;i<=address.getMaxAddressLineIndex();i++){
                    addressFragment.add(address.getAddressLine(i));
                }
                deliverResultToReceiver(SUCCESS_RESULT, TextUtils.join(
                        Objects.requireNonNull(System.getProperty("line.separator")), addressFragment
                ));
            }
        }

    }

    public void deliverResultToReceiver(int resultCode, String addressMessage) {
        Bundle bundle = new Bundle();
        bundle.putString(RESULT_DATA_KEY, addressMessage);
        resultReceiver.send(resultCode, bundle);
    }
}
