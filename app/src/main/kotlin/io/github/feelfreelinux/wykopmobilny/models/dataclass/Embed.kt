package io.github.feelfreelinux.wykopmobilny.models.dataclass

data class Embed(val type : String,
                 val preview : String,
                 val url : String,
                 val plus18 : Boolean,
                 val source : String?,
                 val isAnimated : Boolean,
                 val size : String,
                 var isResize : Boolean = false)