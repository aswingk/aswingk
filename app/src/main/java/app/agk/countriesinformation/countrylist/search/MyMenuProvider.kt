package app.agk.countriesinformation.countrylist.search

import android.os.IBinder
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuProvider
import app.agk.countriesinformation.R

interface IFragmentMenuBridge{

    fun getSearchQuery() : String

    fun hideKeyboard(binder : IBinder)

    fun updateSearch(query : String)

    fun toggleSortFilter()

}

class MyMenuProvider(val fragmentMenuBridge: IFragmentMenuBridge) : MenuProvider {

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_main, menu)

        val searchItem = menu.findItem(R.id.menu_search)
        val searchView = searchItem.actionView as SearchView

        val searchQuery = fragmentMenuBridge.getSearchQuery()

        if (!searchQuery.isEmpty() && !searchItem.isActionViewExpanded) {
            searchItem.expandActionView()
            searchView.setQuery(searchQuery, false)
        }

        val searchViewListener = object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return performSearch(query, true)
            }

            private fun performSearch(query: String, isCompleted: Boolean): Boolean {
                if (isCompleted) {
                    fragmentMenuBridge.hideKeyboard(searchView.windowToken)
                }
                fragmentMenuBridge.updateSearch(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return performSearch(newText, false)
            }
        }

        searchView.setOnQueryTextListener(searchViewListener)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.sortBy -> fragmentMenuBridge.toggleSortFilter()
            else -> {}
        }
        return true
    }
}
