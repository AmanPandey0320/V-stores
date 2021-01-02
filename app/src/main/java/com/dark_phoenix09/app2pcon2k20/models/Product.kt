package com.dark_phoenix09.app2pcon2k20.models


class Product {
    public fun Product(){}

    var image: String? =null
    var name: String? = null
    var description: String? = null
    var price: String? = null
    var code: String? = null
    var type: String? = null

    @JvmName("getImage1")
    fun getImage(): String? {
        return image
    }

    @JvmName("setImage1")
    fun setImage(image: String) {
        this.image = image
    }

    @JvmName("getName1")
    fun getName(): String? {
        return name
    }

    @JvmName("setName1")
    fun setName(name: String) {
        this.name = name
    }

    @JvmName("getDescription1")
    fun getDescription(): String? {
        return description
    }

    @JvmName("setDescription1")
    fun setDescription(description: String) {
        this.description = description
    }

    @JvmName("getPrice1")
    fun getPrice(): String? {
        return price
    }

    @JvmName("setPrice1")
    fun setPrice(price: String) {
        this.price = price
    }

    @JvmName("getCode1")
    fun getCode(): String? {
        return code
    }

    @JvmName("setCode1")
    fun setCode(code: String) {
        this.code = code
    }

    @JvmName("getType1")
    fun getType(): String? {
        return type
    }

    @JvmName("setType1")
    fun setType(type: String) {
        this.type = type
    }

}