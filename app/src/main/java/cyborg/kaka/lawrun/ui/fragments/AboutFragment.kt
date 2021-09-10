package cyborg.kaka.lawrun.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import cyborg.kaka.lawrun.R
import cyborg.kaka.lawrun.databinding.FragmentAboutBinding


class AboutFragment : Fragment() {
    private lateinit var layout: FragmentAboutBinding // ViewBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        layout = FragmentAboutBinding.inflate(inflater, container, false)

        // Kernel Telegram
        layout.cardContactNegrroo.setOnClickListener {
            val intent =
                Intent(Intent.ACTION_VIEW, Uri.parse("tg://resolve?domain=negrroo1"))
            startActivity(intent)
        }

        // App Telegram
        layout.cardContactCyborg.setOnClickListener {
            val intent =
                Intent(Intent.ACTION_VIEW, Uri.parse("tg://resolve?domain=khanra17"))
            startActivity(intent)
        }

        // Negrroo Paypal
        layout.cardSupportNegrroo.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://lawrun-kernel.blogspot.com/2020/08/donation-link.html")
            )
            startActivity(intent)
        }

        // Cyborg Paypal *Temporary adding My Link...
        layout.cardSupportCyborg.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://lawrun-kernel.blogspot.com/2020/08/donation-link.html")
            )
            startActivity(intent)
        }
        return layout.root
    }

    // Reset Colors
    private fun resetColors() {
        // GetTheme Color
        val mainContainer = activity!!.findViewById<LinearLayout>(R.id.mainContainer)
        val colorHolder = mainContainer.findViewById<TextView>(R.id.tab_color_holder)
        val themeColor = colorHolder.textColors.defaultColor
        layout.tvAboutUs.setTextColor(themeColor)
        layout.tvUpcoming.setTextColor(themeColor)
        layout.tvContactDevs.setTextColor(themeColor)
        layout.tvSupportUs.setTextColor(themeColor)
    }

    // Refresh Fragment
    override fun onStart() {
        super.onStart()
        resetColors()
    }
}
