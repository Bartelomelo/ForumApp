package com.example.froumapp.ui

import android.app.Activity
import android.content.Intent
import android.graphics.Rect
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.froumapp.data.network.Resource
import com.example.froumapp.ui.auth.LoginFragment
import com.example.froumapp.ui.base.BaseFragment
import com.google.android.material.snackbar.Snackbar

fun <A : Activity> Activity.startNewActivity(activity: Class<A>) {
    Intent(this, activity).also {
        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(it)
    }
}

fun View.snackbar(message: String, action: (() -> Unit)? = null) {
    val snackbar = Snackbar.make(this, message, Snackbar.LENGTH_LONG)
    action?.let {
        snackbar.setAction("Retry") {
            it()
        }
    }
    snackbar.show()
}

fun Fragment.handleApiError(
    failure: Resource.Failure,
    retryAction: (() -> Unit)? = null
) {
    when {
        failure.isNetworkError -> requireView().snackbar(
            "Sprawdź połączenie z internetem.",
            retryAction
        )

        failure.errorCode == 500 -> {
            if (this is LoginFragment) {
                requireView().snackbar("Podany użytkownik nie istnieje.")
            } else {
                requireView().snackbar("Problem z serwerem.")
                (this as BaseFragment<*>).logout()
            }
        }

        failure.errorCode == 401 -> {
            if (this is LoginFragment) {
                requireView().snackbar("Podano błędne hasło.")
            } else {
                (this as BaseFragment<*>).logout()
                requireView().snackbar("Sesja wygasła, wymagane ponowne zalogowanie.")
            }
        }

        else -> {
            val errorBody = failure.errorBody.toString()
            requireView().snackbar(errorBody)
        }
    }

}

class MarginItemDecoration(private val spaceSize: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect, view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        with(outRect) {
            if (parent.getChildAdapterPosition(view) == 0) {
                top = spaceSize
            }
            bottom = spaceSize
        }
    }
}
