package com.example.pickupsports

import android.content.Context
import android.provider.Telephony.Mms.Sent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class MessageAdapter(val context: Context, val messageList: ArrayList<chat>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val item_receive = 1
    val item_sent = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        if(viewType == 1){
            val view: View = LayoutInflater.from(context).inflate(R.layout.receive, parent, false)
            return ReceiveViewHolder(view)
        }else{
            val view: View = LayoutInflater.from(context).inflate(R.layout.sent, parent, false)
            return SentViewHolder(view)
        }

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currMessage = messageList[position]
        if(holder.javaClass == SentViewHolder::class.java){
            val viewHolder = holder as SentViewHolder
            holder.sentMessage.text = currMessage.message
        }else{
            val viewHolder = holder as ReceiveViewHolder
            holder.receiveMessage.text = currMessage.message

        }

    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun getItemViewType(position: Int): Int {
        val currMessage = messageList[position]
        if(FirebaseAuth.getInstance().currentUser?.uid.equals(currMessage.senderId)){
            return item_sent
        }else{
            return item_receive
        }
    }

    class SentViewHolder(itemView: View) : ViewHolder(itemView){
        val sentMessage = itemView.findViewById<TextView>(R.id.txt_sentMessage)
    }

    class ReceiveViewHolder(itemView: View) : ViewHolder(itemView){
        val receiveMessage = itemView.findViewById<TextView>(R.id.txt_receiveMessage)
    }
}