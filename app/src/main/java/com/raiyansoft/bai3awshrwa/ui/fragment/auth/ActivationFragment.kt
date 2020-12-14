package com.raiyansoft.bai3awshrwa.ui.fragment.auth

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.poovam.pinedittextfield.PinField
import com.raiyansoft.bai3awshrwa.R
import com.raiyansoft.bai3awshrwa.databinding.FragmentActivationBinding
import com.raiyansoft.bai3awshrwa.model.login.Activation
import com.raiyansoft.bai3awshrwa.ui.activity.InterActivity
import com.raiyansoft.bai3awshrwa.ui.viewmodel.LoginViewModel
import com.raiyansoft.bai3awshrwa.util.Commons
import org.jetbrains.annotations.NotNull
import java.util.*

class ActivationFragment : Fragment() {

    private lateinit var mBinding: FragmentActivationBinding
    private var mCountDownTimer: CountDownTimer? = null

    private lateinit var dialog: Dialog
    private lateinit var title: TextView
    private lateinit var details: TextView
    private lateinit var ok: Button

    private var mStartTimeInMillis: Long = 0
    private var mTimeLeftInMillis: Long = 0
    private var mEndTime: Long = 0
    private var mTimerRunning = true

    private var activation_code = ""

    private val viewModel by lazy {
        ViewModelProvider(this)[LoginViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentActivationBinding.inflate(inflater, container, false).apply {
            executePendingBindings()
        }
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val anim = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_up)
        mBinding.clLayout2.startAnimation(anim)

        dialog = Dialog(activity!!)
        dialog.setContentView(R.layout.custom_dialog)
        title = dialog.findViewById(R.id.tvDialogTitle)
        details = dialog.findViewById(R.id.tvDialogText)
        ok = dialog.findViewById(R.id.btnOk)
        dialog.setCancelable(false)
        ok.setOnClickListener {
            dialog.cancel()
        }

        mBinding.pinCode.onTextCompleteListener = object : PinField.OnTextCompleteListener {
            override fun onTextComplete(@NotNull enteredText: String): Boolean {
                activation_code = enteredText
                mBinding.btnCheck.startAnimation{
                    activeAccount()
                }
                return true // Return false to keep the keyboard open else return true to close the keyboard
            }
        }

        mBinding.btnCheck.setOnClickListener {
            if (mBinding.pinCode.text!!.isEmpty()){
                title.text = getString(R.string.login_faild)
                details.text = getString(R.string.empty_fields)
                dialog.show()
                mBinding.btnCheck.revertAnimation {
                    mBinding.btnCheck.text = getString(R.string.try_again)
                }
            }else{
                activation_code = mBinding.pinCode.text.toString()
                mBinding.btnCheck.startAnimation{
                    activeAccount()
                }
            }
        }

        mBinding.tvResend.setOnClickListener {
            mBinding.tvResend.visibility = View.GONE
            viewModel.resendActivation()
            viewModel.dataResend.observe(viewLifecycleOwner,
                {response ->
                    if (response.status && response.code == 200){
                        title.text = getString(R.string.attention)
                        details.text = getString(R.string.resend_messsage)
                        dialog.show()
                        setTime()
                        startTimer()
                    } else{
                        title.text = getString(R.string.login_faild)
                        details.text = getString(R.string.somthing_wrong)
                        dialog.show()
                    }
                }
            )
        }
    }

    override fun onResume() {
        super.onResume()
        mBinding.tvResend.visibility = View.GONE
        setTime()
        startTimer()

    }

    private fun setTime() {
        mStartTimeInMillis = 300000
        resetTimer()
    }


    private fun startTimer() {
        mEndTime = System.currentTimeMillis() + mTimeLeftInMillis
        mCountDownTimer = object : CountDownTimer(mTimeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                mTimeLeftInMillis = millisUntilFinished
                updateCountDownText()
            }

            override fun onFinish() {
                mTimerRunning = false
                mBinding.tvResend.visibility = View.VISIBLE
            }
        }.start()
        mTimerRunning = true

    }

    private fun resetTimer() {
        mTimeLeftInMillis = mStartTimeInMillis
        updateCountDownText()
    }

    private fun updateCountDownText() {
        val hours = (mTimeLeftInMillis / 1000).toInt() / 3600
        val minutes = (mTimeLeftInMillis / 1000 % 3600).toInt() / 60
        val seconds = (mTimeLeftInMillis / 1000).toInt() % 60
        val timeLeftFormatted: String?
        timeLeftFormatted = if (hours > 0) {
            java.lang.String.format(
                Locale.getDefault(),
                "%d:%02d:%02d", hours, minutes, seconds
            )
        } else {
            java.lang.String.format(
                Locale.getDefault(),
                "%02d:%02d", minutes, seconds
            )
        }
        mBinding.tvTimer.text = timeLeftFormatted ?: ""
    }


    override fun onStop() {
        requireActivity().finish()
        mCountDownTimer!!.cancel()
        super.onStop()
    }

    private fun activeAccount(){
        val activate = Activation(activation_code)
        viewModel.activateAccount(activate)
        viewModel.dataActivate.observe(viewLifecycleOwner,
            {response ->
                if (response.status && response.code == 200){
                    requireActivity().startActivity(Intent(requireContext(), InterActivity::class.java))
                    requireActivity().finish()
                    mBinding.btnCheck.revertAnimation()
                }else if (response.code == 110){
                    title.text = getString(R.string.login_faild)
                    details.text = getString(R.string.wrong_code)
                    dialog.show()
                    mBinding.btnCheck.revertAnimation {
                        mBinding.btnCheck.text = getString(R.string.try_again)
                    }
                }else{
                    title.text = getString(R.string.login_faild)
                    details.text = getString(R.string.somthing_wrong)
                    dialog.show()
                    mBinding.btnCheck.revertAnimation {
                        mBinding.btnCheck.text = getString(R.string.try_again)
                    }
                }
            }
        )
    }
}