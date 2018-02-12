package com.turnipconsultants.brongo_client.others.AllUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.location.Location;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.turnipconsultants.brongo_client.CustomWidgets.BrongoTextView;
import com.turnipconsultants.brongo_client.CustomWidgets.GifView;
import com.turnipconsultants.brongo_client.Listener.NoInternetTryConnectListener;
import com.turnipconsultants.brongo_client.Listener.RetryPaymentListener;
import com.turnipconsultants.brongo_client.R;
import com.turnipconsultants.brongo_client.activities.PaymentSubscriptionActivity;
import com.turnipconsultants.brongo_client.models.BudgetRangeModel;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import butterknife.ButterKnife;

/**
 * Created by Pankaj on 14-12-2017.
 */

public class AllUtils {
    private static final String TAG = "AllUtils";
    private String newToken;
    public static Dialog dialog;
    private SharedPreferences pref;
    public static ProgressBar progressDialog;
    private static AlertDialog alertDialog;

    public static class CalculatorUtils {
        private static final String TAG = "CalculatorUtils";

        public static BudgetRangeModel getRangeSetFor(Double from, Double to, Double diff) {
            ArrayList<Double> doubleArrayList = new ArrayList<>();
            ArrayList<String> stringArrayList = new ArrayList<>();

            DecimalFormat df = new DecimalFormat("#.##");
            while (from <= to) {
                stringArrayList.add(Suffix(df, from));
                doubleArrayList.add(from);
                from += diff;
            }

            Log.i(TAG, "getRangeSetFor: doubleArrayList " + doubleArrayList);
            Log.i(TAG, "getRangeSetFor: stringArrayList " + stringArrayList);
            BudgetRangeModel budgetRangeModel = new BudgetRangeModel(doubleArrayList, stringArrayList);
            return budgetRangeModel;
        }

        public static String Suffix(DecimalFormat df, Double amount) {
            String description = "";

            int Thousand = 1000, Lakh = 100000, Crore = 10000000;

            if (amount >= Crore) {
                amount = amount / Crore;
                description = "Cr";
            } else if (amount >= Lakh) {
                amount = amount / Lakh;
                description = "L";
            } else if (amount >= Thousand) {
                amount = amount / Thousand;
                description = "K";
            }
            return df.format(amount) + description;
        }
    }

    public static class ToastUtils {
        public static void showToast(Context context, String message) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }
    }

    public static class LoaderUtils {

        public static void showLoader(Context context) {
            dialog = new Dialog(context, R.style.CustomProgressDialog);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.progress_layout);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            GifView check_mark_GV = dialog.findViewById(R.id.check_mark_GV);
            check_mark_GV.setGifResource(R.drawable.loader);
            check_mark_GV.play();
            Log.e("Loader", context.getClass().getSimpleName());
        }

        public static void dismissLoader() {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }

    public static class KeyboardUtils {

        public static void hideKeyBoard(Activity activity) {
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        }

        public static void hideKeyBoard(View view, Context context) {

            if (view != null) {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }

        }

        public static void ShowKeyboard(Context context, View view) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }

    }

    public static class DialogUtils {
        public static void NoInternetDialog(final Context context, final NoInternetTryConnectListener internetTryConnectListener) {
            final Dialog dialogBlock = new Dialog(context, R.style.DialogTheme);
            dialogBlock.setContentView(R.layout.no_internet_dialog);
            dialogBlock.setCanceledOnTouchOutside(false);
            dialogBlock.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

            Button reconnect = ButterKnife.findById(dialogBlock, R.id.try_again);
            reconnect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogBlock.dismiss();
                    if (internetTryConnectListener != null)
                        internetTryConnectListener.onTryReconnect();
                }
            });

            ImageView close = ButterKnife.findById(dialogBlock, R.id.close);
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogBlock.dismiss();
                }
            });

            dialogBlock.show();
        }

        public static void PaymentSuccessDialog(final Context context, final RetryPaymentListener retryPaymentListener) {
            final Dialog dialogBlock = new Dialog(context, R.style.DialogTheme);
            dialogBlock.setContentView(R.layout.payment_success);
            dialogBlock.setCanceledOnTouchOutside(false);
            dialogBlock.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

            Button reconnect = ButterKnife.findById(dialogBlock, R.id.ok_BTN);
            reconnect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogBlock.dismiss();
                    if (retryPaymentListener != null)
                        retryPaymentListener.paymentSuccess();
                }
            });

            ImageView close = ButterKnife.findById(dialogBlock, R.id.close);
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogBlock.dismiss();
                }
            });

            dialogBlock.show();
        }

        public static void PaymentFailedDialog(final Context context, final RetryPaymentListener retryPaymentListener) {
            final Dialog dialogBlock = new Dialog(context, R.style.DialogTheme);
            dialogBlock.setContentView(R.layout.payment_failed);
            dialogBlock.setCanceledOnTouchOutside(false);
            dialogBlock.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

            Button reconnect = ButterKnife.findById(dialogBlock, R.id.try_again_payment);
            reconnect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogBlock.dismiss();
                    if (retryPaymentListener != null)
                        retryPaymentListener.RetryPayment();
                }
            });

            ImageView close = ButterKnife.findById(dialogBlock, R.id.close);
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogBlock.dismiss();
                }
            });

            dialogBlock.show();
        }

        public static void PaymentErrorDialog(final Context context, final RetryPaymentListener retryPaymentListener) {
            final Dialog dialogBlock = new Dialog(context, R.style.DialogTheme);
            dialogBlock.setContentView(R.layout.payment_error);
            dialogBlock.setCanceledOnTouchOutside(false);
            dialogBlock.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

            Button reconnect = ButterKnife.findById(dialogBlock, R.id.try_again_payment);
            reconnect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogBlock.dismiss();
                    if (retryPaymentListener != null)
                        retryPaymentListener.RetryPayment();
                }
            });

            ImageView close = ButterKnife.findById(dialogBlock, R.id.close);
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogBlock.dismiss();
                }
            });

            dialogBlock.show();
        }
    }

    public static class FragmentUtils {
        public static void addFragment(FragmentManager fm, int containerId, Fragment fragment, String tag) {
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(containerId, fragment, tag);
            ft.addToBackStack(tag);
            ft.commit();
        }
    }

    public static class MapUtils {
        public static LatLng getRandomLocation(LatLng point, int radiusMeter) {

            List<LatLng> randomPoints = new ArrayList<>();
            List<Float> randomDistances = new ArrayList<>();
            Location myLocation = new Location("");
            myLocation.setLatitude(point.latitude);
            myLocation.setLongitude(point.longitude);

            //This is to generate 10 random points
            for (int i = 0; i < 10; i++) {
                double x0 = point.latitude;
                double y0 = point.longitude;

                Random random = new Random();

                // Convert radius from meters to degrees
                double radiusInDegrees = radiusMeter / 111000f;

                double u = random.nextDouble();
                double v = random.nextDouble();
                double w = radiusInDegrees * Math.sqrt(u);
                double t = 2 * Math.PI * v;
                double x = w * Math.cos(t);
                double y = w * Math.sin(t);

                // Adjust the x-coordinate for the shrinking of the east-west distances
                double new_x = x / Math.cos(y0);

                double foundLatitude = new_x + x0;
                double foundLongitude = y + y0;
                LatLng randomLatLng = new LatLng(foundLatitude, foundLongitude);
                randomPoints.add(randomLatLng);
                Location l1 = new Location("");
                l1.setLatitude(randomLatLng.latitude);
                l1.setLongitude(randomLatLng.longitude);
                randomDistances.add(l1.distanceTo(myLocation));
            }
            //Get nearest point to the centre
            int indexOfNearestPointToCentre = randomDistances.indexOf(Collections.min(randomDistances));
            return randomPoints.get(indexOfNearestPointToCentre);
        }
    }

    public static class DensityUtils {

        public static int dpToPx(int dp) {
            return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem().getDisplayMetrics());
        }

        public static int pxToDp(float px) {
            return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, px, Resources.getSystem().getDisplayMetrics());
        }
    }

    public static class StringUtilsBrongo {
        public static String toCamelCase(String inputString) {
            String result = "";
            if (inputString.length() == 0) {
                return result;
            }
            char firstChar = inputString.charAt(0);
            char firstCharToUpperCase = Character.toUpperCase(firstChar);
            result = result + firstCharToUpperCase;
            for (int i = 1; i < inputString.length(); i++) {
                char currentChar = inputString.charAt(i);
                char previousChar = inputString.charAt(i - 1);
                if (previousChar == ' ') {
                    char currentCharToUpperCase = Character.toUpperCase(currentChar);
                    result = result + currentCharToUpperCase;
                } else {
                    char currentCharToLowerCase = Character.toLowerCase(currentChar);
                    result = result + currentCharToLowerCase;
                }
            }
            return result;
        }
    }

    public static class FontUtils {
        public static Typeface getBrongoRegularFont(Context context) {
            return Typeface.createFromAsset(context.getAssets(), "Lato-Regular.ttf");
        }
    }

    public static boolean isPackageInstalled(String packagename, Context context) {

        PackageManager pm = context.getPackageManager();
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(packagename, 0);
            pm.getPackageInfo(packagename, PackageManager.GET_ACTIVITIES);
            if (ai.enabled) {
                return true;
            } else {
                return false;
            }

        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static void showMaxRequestReached(final Context context, String message, final RequestReachedListener listener) {
        final Dialog dialogBlock = new Dialog(context, R.style.DialogTheme);
        dialogBlock.setContentView(R.layout.thankyou_dialog);
        dialogBlock.setCanceledOnTouchOutside(false);
        dialogBlock.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        BrongoTextView textView = dialogBlock.findViewById(R.id.thankyoutv);
        textView.setText(message + " Please Subscribe.");
        Button back = dialogBlock.findViewById(R.id.backbtn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBlock.dismiss();
                listener.onRequestReached();
            }
        });

        ImageView cancel = dialogBlock.findViewById(R.id.cancel);
        cancel.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogBlock.dismiss();
                    }
                });

        dialogBlock.show();
    }

    public interface RequestReachedListener {
        void onRequestReached();
    }
}
