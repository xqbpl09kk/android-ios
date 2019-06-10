package com.mh.ca

import com.mh.ca.flow.DecorContext
import java.util.*
import kotlin.properties.Delegates

/**
 * Created by tinytitan on 2017/3/12.
 * VIPER 架构的和心类
 */
class CAViper {

	internal var parent: CAViper? = null
	val actor: Actor
	val presenter: Presenter
	val router: Router
	
	internal val context: DataContext
	
	
	init {
		actor = Actor()
		presenter = Presenter()
		router = Router()
		context = DataContext()
		
		actor.bindViper(this)
		presenter.bindViper(this)
		router.bindViper(this)
		context.bindViper(this)
	}
	
	internal class DataContext {
		private var _vip: CAViper by Delegates.notNull<CAViper>()
		// 这个就是计算属性，不提供set函数即可
		var vip: CAViper
			get() = this._vip
			set(value) {}
		fun bindViper(vip: CAViper) {
			this._vip = vip
		}
		
		internal var header: Any? = null
			set(value){
				field = value
				this.present(DataPresentType.header, mutableListOf(this.vip), header)
			}
		
		internal var footer: Any? = null
			set(value){
				field = value
				this.present(DataPresentType.footer, mutableListOf(this.vip), footer)
			}
		
		
		internal var content: Any? = null
			set(value){
				field = value
				this.present(DataPresentType.content, mutableListOf(this.vip), content)
			}
		
		
		internal var position: Int = 0
			set(value){
				field = value
				this.present(DataPresentType.position, mutableListOf(this.vip), position)
			}
		
		
		// MARK: 各种附加数据可以用来自解释 == 上下文主要用来 【暂存数据】
		private var extraDict: MutableMap<String, Any> = HashMap<String, Any>()
		
		internal fun extra(key: String): Any? {
			return extraDict[key]
		}
		
		// MARK: 附加数据的传递 == 需要定制内部实现
		internal fun receiveExtra(sender: DataContext) {
			for((key, value) in sender.extraDict) {
				this.extra(key, value)
			}
		}
		
		// MARK:  自定义数据的解析一定是 (String, Any)  tunple序列
		internal fun extra(key: String, value: Any) {
			extraDict[key] = value
			this.present(DataPresentType.extra, mutableListOf(this.vip), value, key)
		}
		
		// MARK: 内部包装一个更合适的外观, 直接是 基于 enmu 类型的，霸道, Extra 应该有 二级 KEY
		private fun present(type: DataPresentType, trace: MutableList<CAViper>, ctx: Any? = null,
		                    subKey: String = "") {
			this.vip.presenter.present(type.key() + subKey, trace, ctx)
		}
		

	}

	class Actor {
		private var _vip: CAViper by Delegates.notNull<CAViper>()
		// 这个就是计算属性，不提供set函数即可
		var vip: CAViper
			get() = this._vip
			set(value) {}
		fun bindViper(vip: CAViper) {
			this._vip = vip
		}
		// MARK: 对应PRD中的 【业务规则】 == 动态路由版本的 IBAction机制
		private val rules: MutableMap<String, (ctx: Any?, trace: MutableList<CAViper>) -> Boolean>
				= HashMap<String, (ctx: Any?, trace: MutableList<CAViper>) -> Boolean>()

		fun rule(id: String, rule: (ctx: Any?, trace: MutableList<CAViper>) -> Boolean) {
			rules[id] = rule
		}

		// MARK: 对应PRD中的 【交互接口】，点击按钮，【删除】搭配
		fun act(id: String, ctx: Any? = null, trace: MutableList<CAViper> = ArrayList<CAViper>()) {
			var trace = trace
			trace.add(this.vip)
			val handler = rules[id]
			if (handler == null) {
				vip.parent?.actor?.act(id, ctx, trace)
				return
			}
			if (!handler(ctx, trace))  {
				vip.parent?.actor?.act(id, ctx, trace)
				return
			}
		}
	}
	
	// MARK:  数据上下文，用来将 Entity 数据包装在 上下文中，
	// 采用原生的 VIPER架构 【同步模型数据】, 专用于数据的自身展现【同步】
	// 理论上 所有的 IBOutlet 都必须是 private的，保证 不存在耦合，非常霸道
	internal enum class DataPresentType {
		header,
		footer,
		content,
		payload, // android 局部更新 专用补丁
		extra,
		position;
		
		fun key(): String {
			return when(this) {
				DataPresentType.header -> "viper.data.containerView.header"
				DataPresentType.footer -> "viper.data.containerView.footer"
				DataPresentType.content -> "viper.data.containerView.content"
				DataPresentType.payload -> "viper.data.containerView.payload."    // 包含 subKey
				DataPresentType.extra -> "viper.data.containerView.extra."        // 包含 subKey
				DataPresentType.position -> "viper.data.containerView.position"
			}
		}
	}
	
	interface ContextHandler {
		fun handle(context: Any?)
	}
	
	class Presenter {
		
		// 架构级别支持的 【生命周期展示逻辑HOOK】,别名，其实也是规则
		fun loadContent(block: (content: Any?)-> Unit) {
			this.rule(DataPresentType.content, block)
		}
		
		fun loadHeader(block: (header: Any?)-> Unit) {
			this.rule(DataPresentType.header, block)
		}
		
		fun loadFooter( block:  (footer: Any?)-> Unit) {
			this.rule(DataPresentType.footer, block)
		}
		
		fun loadPosition( block: ( position: Int)-> Unit) {
			this.rule(DataPresentType.position, "") { data ->
				val position = data as? Int ?: return@rule
				block(position)
			}
		}
		
		fun loadDocorContext( block: (context: DecorContext)-> Unit) {
			this.rule(DataPresentType.extra, DecorContext.KEY) { data ->
				val context = data as? DecorContext ?: return@rule
				block(context)
			}
		}
		
		fun loadExtra( subKey: String, block:  ( data: Any?)-> Unit) {
			this.rule(DataPresentType.extra, subKey, block)
		}
		
		// 局部刷新 == 指令处理, 如果不提供 key 代表 【默认的局部刷新行为】
		fun payload( subKey: String = "", block:  ( data: Any?)-> Unit) {
			this.rule(DataPresentType.payload, subKey, block)
		}
		
		// MARK:  添加一个 FACADA 专门处理内部数据加载，展示规则
		internal fun rule(type: DataPresentType, subKey: String = "", feedback:(ctx: Any?) -> Unit) {
			rules[type.key() + subKey] = { ctx ->
				feedback(ctx)
				true
			}
		}
		
		internal fun rule(type: DataPresentType, feedback:(ctx: Any?) -> Unit) {
			rule(type, "", feedback)
		}
		
		
		private var _vip: CAViper by Delegates.notNull<CAViper>()
		// 这个就是计算属性，不提供set函数即可
		var vip: CAViper
			get() = this._vip
			set(value) {}
		fun bindViper(vip: CAViper) {
			this._vip = vip
		}
		// MARK:  对应 PRD中的页面跳转逻辑
		private val rules: MutableMap<String, (ctx: Any?) -> Boolean>
				= HashMap<String, (ctx: Any?) -> Boolean>()
		
		fun rule(id: String, rule: (ctx: Any?) -> Boolean) {
			rules[id] = rule
		}
		// MARK: 对应PRD中的 【交互接口】，点击按钮，【删除】搭配
		fun present(id: String, trace: MutableList<CAViper>, ctx: Any? = null) {
			trace
					.asSequence()
					.mapNotNull { it.presenter.rules[id] }
					.filter { it(ctx) }
					.forEach { return }

		}
	}

	class Router {
		private var _vip: CAViper by Delegates.notNull<CAViper>()
		// 这个就是计算属性，不提供set函数即可
		var vip: CAViper
			get() = this._vip
			set(value) {}
		fun bindViper(vip: CAViper) {
			this._vip = vip
		}
		// MARK:  对应 PRD中的页面跳转逻辑
		private val rules: MutableMap<String, (ctx: Any?) -> Unit>
				= HashMap<String, (ctx: Any?) -> Unit>()
		fun rule(id: String, rule: (ctx: Any?) -> Unit) {
			rules[id] = rule
		}
		// MARK: 对应PRD中的 【交互接口】，点击按钮，【删除】搭配
		fun route(id: String, ctx: Any? = null) {
			val router = rules[id]
			if (router == null) {
				vip.parent?.router?.route(id, ctx)
				return
			}
			router(ctx)
		}
	}
	

	// MARK:  可以直接使用，非常霸道， 执行规则直接关联 【动作】,一般要添加 前缀防止冲突
	fun route(id: String, ctx: Any? = null) {
		this.router.route(id, ctx)
	}
	// 对外部直接提供的接口进行相应， 这个几乎不会使用
	fun present(id: String, trace: MutableList<CAViper> = mutableListOf(this), ctx: Any? = null) {
		this.presenter.present(id, trace, ctx)
	}
	
	//  HOOK 到 RecyclerView 的局部刷新技术
	fun payload(key: String, value: Any? = null) {
		this.presenter.present(DataPresentType.payload.key() + key, mutableListOf(this), value)
	}
	
	fun act(id: String, ctx: Any? = null, trace: MutableList<CAViper> = mutableListOf()) {
		this.actor.act(id, ctx, trace)
	}
	
	var content: Any?
		get() = this.context.content
		set(value) {
			this.context.content = value
		}
	
	
	var position: Int
		get()  = this.context.position
		set(value) {
			this.context.position = value
		}
	
	
	var header: Any?
		get() =  this.context.header
		set(value) {
			this.context.header = value
		}
	
	var footer: Any?
		get() = this.context.footer
		set(value) {
			this.context.footer = value
		}
	
	fun  extra(key: String) : Any? {
		return this.context.extra(key)
	}
	
	fun extra(key: String, value: Any) {
		this.context.extra(key, value)
	}
}