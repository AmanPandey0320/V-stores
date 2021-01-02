package com.dark_phoenix09.app2pcon2k20.models

class myorder {

    public fun myorder(){}

    var orderID: String? = null
    var code: String? = null
    var quantity: String? = null
    var total: String? = null
    var pname: String? = null
    var bname: String? = null
    var baddress: String? = null
    var bmobile: String? = null
    var pmode: String? = null
    var status: String? = null

    @JvmName("getOrderID1")
    fun getOrderID(): String? {
        return orderID
    }

    @JvmName("setOrderID1")
    fun setOrderID(orderID: String?) {
        this.orderID = orderID
    }

    @JvmName("getCode1")
    fun getCode(): String? {
        return code
    }

    @JvmName("setCode1")
    fun setCode(code: String?) {
        this.code = code
    }

    @JvmName("getQuantity1")
    fun getQuantity(): String? {
        return quantity
    }

    @JvmName("setQuantity1")
    fun setQuantity(quantity: String?) {
        this.quantity = quantity
    }

    @JvmName("getTotal1")
    fun getTotal(): String? {
        return total
    }

    @JvmName("setTotal1")
    fun setTotal(total: String?) {
        this.total = total
    }

    @JvmName("getPname1")
    fun getPname(): String? {
        return pname
    }

    @JvmName("setPname1")
    fun setPname(pName: String?) {
        this.pname = pName
    }

    @JvmName("getBname1")
    fun getBname(): String? {
        return bname
    }

    @JvmName("setBname1")
    fun setBname(bName: String?) {
        this.bname = bName
    }

    @JvmName("getBaddress1")
    fun getBaddress(): String? {
        return baddress
    }

    @JvmName("setBaddress1")
    fun setBaddress(bAddress: String?) {
        this.baddress = bAddress
    }

    @JvmName("getBmobile1")
    fun getBmobile(): String? {
        return bmobile
    }

    @JvmName("setBmobile1")
    fun setBmobile(bMobile: String?) {
        this.bmobile = bMobile
    }

    @JvmName("getPmode1")
    fun getPmode(): String? {
        return pmode
    }

    @JvmName("setPmode1")
    fun setPmode(pMode: String?) {
        this.pmode = pMode
    }

    @JvmName("getStatus1")
    fun getStatus(): String? {
        return status
    }

    @JvmName("setStatus1")
    fun setStatus(status: String?) {
        this.status = status
    }



}