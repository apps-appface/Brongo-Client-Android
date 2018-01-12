package com.turnipconsultants.brongo_client.others;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.turnipconsultants.brongo_client.Listener.CommissionListenerFactory;
import com.turnipconsultants.brongo_client.R;

/**
 * Created by Pankaj on 07-12-2017.
 */

public class CommissionDialogFactory {

    public static void BuyCommissionDialog(Context context, final CommissionListenerFactory.BuyCommissionListener buyCommissionListener) {
        final AnimatorSet[] as = {null};
        final Dialog dialogBlock = new Dialog(context, R.style.DialogTheme);
        dialogBlock.setContentView(R.layout.buy_commision_dialog);
        dialogBlock.setCanceledOnTouchOutside(false);
        dialogBlock.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        final RadioButton resaleRb = dialogBlock.findViewById(R.id.resaleRb);
        final RadioButton rb1 = dialogBlock.findViewById(R.id.rb1);
        final RadioButton rb2 = dialogBlock.findViewById(R.id.rb2);
        final Button accept = dialogBlock.findViewById(R.id.accept);


        LinearLayout whyLL = dialogBlock.findViewById(R.id.whyLL);
        final RelativeLayout toastMessageRL = dialogBlock.findViewById(R.id.toast_messageRL);
        ImageView cancel=dialogBlock.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBlock.dismiss();
            }
        });
        buyCommissionListener.SetCommission(resaleRb.getText().toString().split("%")[0]);
        whyLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (as[0] != null) {
                    as[0].cancel();
                } else {
                    as[0] = new AnimatorSet();
                }

                ObjectAnimator fadeIn = ObjectAnimator.ofFloat(toastMessageRL, "alpha", 0.2f, 1f);
                fadeIn.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        toastMessageRL.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {

                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                fadeIn.setDuration(1000);
                ObjectAnimator fadeOut = ObjectAnimator.ofFloat(toastMessageRL, "alpha", 1f, 0.2f);
                fadeOut.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        toastMessageRL.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                fadeOut.setDuration(2000);
                fadeOut.setStartDelay(4000);
                as[0].playSequentially(fadeIn, fadeOut);
                as[0].start();
            }
        });
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBlock.dismiss();
                buyCommissionListener.Accept();
            }
        });
        resaleRb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    rb1.setChecked(false);
                    rb2.setChecked(false);
                    buyCommissionListener.SetCommission(compoundButton.getText().toString().split("%")[0]);
                }
            }
        });

        rb1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    resaleRb.setChecked(false);
                    rb2.setChecked(false);
                    buyCommissionListener.SetCommission(compoundButton.getText().toString().split("%")[0]);
                }
            }
        });

        rb2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    resaleRb.setChecked(false);
                    rb1.setChecked(false);
                    buyCommissionListener.SetCommission(compoundButton.getText().toString().split("%")[0]);
                }
            }
        });
        dialogBlock.show();
    }

    public static void RentCommissionDialog(Context context, final CommissionListenerFactory.RentCommissionListener rentCommissionListener) {
        final Dialog dialogBlock = new Dialog(context, R.style.DialogTheme);
        dialogBlock.setContentView(R.layout.rent_commision_dialog);
        dialogBlock.setCanceledOnTouchOutside(false);
        dialogBlock.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        final RadioButton resaleRb = dialogBlock.findViewById(R.id.resaleRb);
        final RadioButton rb1 = dialogBlock.findViewById(R.id.rb1);
        final Button accept = dialogBlock.findViewById(R.id.accept);

        LinearLayout whyLL = dialogBlock.findViewById(R.id.whyLL);
        final RelativeLayout toastMessageRL = dialogBlock.findViewById(R.id.toast_messageRL);

        rentCommissionListener.SetAdvanceRentMonths("100");
        whyLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObjectAnimator fadeIn = ObjectAnimator.ofFloat(toastMessageRL, "alpha", .2f, 1f);
                fadeIn.setDuration(1000);
                fadeIn.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                toastMessageRL.setVisibility(View.INVISIBLE);
                            }
                        }, 5000);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                fadeIn.start();
                toastMessageRL.setVisibility(View.VISIBLE);
            }
        });
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBlock.dismiss();
                rentCommissionListener.Accept();
            }
        });
        resaleRb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    rb1.setChecked(false);
                    rentCommissionListener.SetAdvanceRentMonths("100");
                }
            }

        });

        rb1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    resaleRb.setChecked(false);
                    rentCommissionListener.SetAdvanceRentMonths("50");
                }
            }
        });

        ImageView cancel=dialogBlock.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBlock.dismiss();
            }
        });

        dialogBlock.show();
    }

    public static void SellCommissionDialog(Context context, final CommissionListenerFactory.SellCommissionListener sellCommissionListener) {
        final Dialog dialogBlock = new Dialog(context, R.style.DialogTheme);
        dialogBlock.setContentView(R.layout.sell_commission_dialog);
        dialogBlock.setCanceledOnTouchOutside(false);
        dialogBlock.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        final Button accept = dialogBlock.findViewById(R.id.accept);
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBlock.dismiss();
                sellCommissionListener.Accept();
            }
        });

        ImageView cancel=dialogBlock.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBlock.dismiss();
            }
        });

        dialogBlock.show();
    }

    public static void RentOutCommissionDialog(Context context, final CommissionListenerFactory.RentOutCommissionListener rentOutCommissionListener) {
        final Dialog dialogBlock = new Dialog(context, R.style.DialogTheme);
        dialogBlock.setContentView(R.layout.rent_out_commission_dialog);
        dialogBlock.setCanceledOnTouchOutside(false);
        dialogBlock.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        final Button accept = dialogBlock.findViewById(R.id.accept);
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBlock.dismiss();
                rentOutCommissionListener.Accept();
            }
        });

        ImageView cancel=dialogBlock.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBlock.dismiss();
            }
        });

        dialogBlock.show();
    }
}
