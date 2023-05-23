package com.devjeong.bookwiki_cbnu.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import com.devjeong.bookwiki_cbnu.R

class ExpandableListAdapter(
    private val context: Context,
    private val groupList: List<String>,
    private val childMap: Map<String, List<String>>
) : BaseExpandableListAdapter() {

    override fun getGroupCount(): Int {
        return groupList.size
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        val groupName = groupList[groupPosition]
        return childMap[groupName]?.size ?: 0
    }

    override fun getGroup(groupPosition: Int): Any {
        return groupList[groupPosition]
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        val groupName = groupList[groupPosition]
        return childMap[groupName]?.get(childPosition) ?: ""
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    override fun getGroupView(
        groupPosition: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        val view: View
        val holder: GroupViewHolder

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.summary_parent, parent, false)
            holder = GroupViewHolder(view)
            view.tag = holder
        } else {
            view = convertView
            holder = convertView.tag as GroupViewHolder
        }

        val groupNameTextView = holder.groupNameTextView
        val groupName = getGroup(groupPosition) as String
        groupNameTextView.text = groupName

        return view
    }

    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        val view: View
        val holder: ChildViewHolder

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_summary, parent, false)
            holder = ChildViewHolder(view)
            view.tag = holder
        } else {
            view = convertView
            holder = convertView.tag as ChildViewHolder
        }

        val childTextView = holder.childTextView
        val child = getChild(groupPosition, childPosition) as String
        childTextView.text = child

        return view
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }

    private class GroupViewHolder(view: View) {
        val groupNameTextView: TextView = view.findViewById(R.id.expand_title)
    }

    private class ChildViewHolder(view: View) {
        val childTextView: TextView = view.findViewById(R.id.summaryTextView)
    }
}

