package com.example.tracking_corona.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.tracking_corona.AffectedCountries
import com.example.tracking_corona.R
import com.example.tracking_corona.model.CountryModel
import java.text.DecimalFormat
import java.util.ArrayList

class CountriesAdapter(val ctx: Context, val countryModelsList: List<CountryModel>) : ArrayAdapter<CountryModel?>(ctx, R.layout.list_custom_item, countryModelsList) {
    private  var countryModelsListFiltered: List<CountryModel>
    init {
        countryModelsListFiltered = countryModelsList
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.list_custom_item, null, true)
        val tvCountryName = view.findViewById<TextView>(R.id.tvCountryName)
        val imageView = view.findViewById<ImageView>(R.id.imageFlag);

        val dealthCt = view.findViewById<TextView>(R.id.dealthCt)
        val dealthDaily = view.findViewById<TextView>(R.id.dealthDaily)
        val recoveredCt = view.findViewById<TextView>(R.id.recoveredCt)
        val recoveredCtDaily = view.findViewById<TextView>(R.id.recoveredCtDaily)
        val affectedCt = view.findViewById<TextView>(R.id.affectedCt)
        val affectedCtDaily = view.findViewById<TextView>(R.id.activeCtDaily)

        val pattern : String = "###,###"
        val decimalFormat : DecimalFormat = DecimalFormat(pattern)

        dealthCt.text = decimalFormat.format(countryModelsListFiltered[position].deaths).toString()
        dealthDaily.text = " (+".plus(decimalFormat.format(countryModelsListFiltered[position].todayDeaths).toString())+")"
        recoveredCt.text = decimalFormat.format(countryModelsListFiltered[position].recovered).toString()
        recoveredCtDaily.text = " (+".plus(decimalFormat.format(countryModelsListFiltered[position].todayRecovered).toString())+")"
        affectedCt.text = decimalFormat.format(countryModelsListFiltered[position].cases).toString()
        affectedCtDaily.text = " (+".plus(decimalFormat.format(countryModelsListFiltered[position].todayCases).toString())+")"
        tvCountryName.text = countryModelsListFiltered[position].country
        Glide.with(ctx).load(countryModelsListFiltered[position].countryInfo.flag).into(imageView)
        return view
    }

    override fun getCount(): Int {
        return countryModelsListFiltered.size
    }

    override fun getItem(position: Int): CountryModel? {
        return countryModelsListFiltered[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence): FilterResults {
                val filterResults = FilterResults()
                if (constraint == null || constraint.length == 0) {
                    filterResults.count = countryModelsList.size
                    filterResults.values = countryModelsList
                } else {
                    val resultsModel: MutableList<CountryModel> =
                        ArrayList()
                    val searchStr = constraint.toString().toLowerCase()
                    for (itemsModel in countryModelsList) {
                        if (itemsModel.country.toLowerCase().contains(searchStr)) {
//                          if (itemsModel.getCountry().toLowerCase().contains(searchStr)) {
                            resultsModel.add(itemsModel)
                        }
                        filterResults.count = resultsModel.size
                        filterResults.values = resultsModel
                    }
                }
                return filterResults
            }

            override fun publishResults(
                constraint: CharSequence,
                results: FilterResults
            ) {
                countryModelsListFiltered = results.values as List<CountryModel>
                AffectedCountries.countryModelsList = results.values as MutableList<CountryModel>
                notifyDataSetChanged()
            }
        }
    }


}