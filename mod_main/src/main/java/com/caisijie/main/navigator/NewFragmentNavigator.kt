package com.caisijie.main.navigator

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.IdRes
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.fragment.FragmentNavigator
import java.util.ArrayDeque

/**
 * 对FragmentNavigator的重写
 *
 * 问题：FragmentNavigator是用过ft.replace(mContainerId,frag);添加Fragment的，replace方式的会导致生命周期重走
 * 又因为里面需要用到mBackStack后退栈，而可见性是private，所以子类中是无法使用的
 * 解决方案：重写FragmentNavigator#navigator()方法，通过反射获取mBackStack后退栈
 *
 * 可以重写navigator()方法{@link Destination#navigate(Destination,Bundle,NavOptions,Navigator.Extras)}
 * 将显示Fragment#replace()改成hide()和show()方法
 *
 * 需要在类头部添加Navigator.name注解，添加一个名称
 */
@Navigator.Name("NewFragmentNavigator")
class NewFragmentNavigator(context: Context, manager: FragmentManager, containerId: Int) :
    FragmentNavigator(context, manager, containerId) {
    private val mContext = context
    private val mManager = manager
    private val mContainerId = containerId

    private val TAG = "NewFragmentNavigator"

    @RequiresApi(Build.VERSION_CODES.GINGERBREAD)
    override fun navigate(
        destination: Destination,
        args: Bundle?,
        navOptions: NavOptions?,
        navigatorExtras: Navigator.Extras?
    ): NavDestination? {
        if (mManager.isStateSaved) {
            //如状态已经保存，打印已保存状态
            Log.i(TAG, "Ignoring navigate() call:FragmentManager has already saved its state")
            //状态是已经保存，就不应该再去保存
            return null
        }
        var className = destination.className
        if (className[0] == '.') {
            className = mContext.packageName + className
        }
        //开启事务，保证要么一定能够完成，要么不完成
        val ft = mManager.beginTransaction()

        var enterAnim = navOptions?.enterAnim ?: -1
        var exitAnim = navOptions?.exitAnim ?: -1
        var popEnterAnim = navOptions?.popEnterAnim ?: -1
        var popExitAnim = navOptions?.popExitAnim ?: -1
        if (enterAnim != -1 || exitAnim != -1 || popEnterAnim != -1 || popExitAnim != -1) {
            enterAnim = if (enterAnim != -1) enterAnim else 0
            exitAnim = if (exitAnim != -1) exitAnim else 0
            popEnterAnim = if (popEnterAnim != -1) popEnterAnim else 0
            popExitAnim = if (popExitAnim != -1) popExitAnim else 0
            ft.setCustomAnimations(enterAnim, exitAnim, popEnterAnim, popExitAnim)
        }

        //        ft.replace(mContainerId, frag)

        //1.先查询当前显示的fragment，不为空则将其hide
        val fragment = mManager.primaryNavigationFragment //当前显示的fragment
        fragment?.let {
            ft.hide(it)
        }

        var frag: Fragment?
        val tag = destination.id.toString()

        frag = mManager.findFragmentByTag(tag)

        //2.根据tag查询当前添加的fragment是否不为空，不为空则将其直接show
        if (frag != null) {
            ft.show(frag)
        } else {
            //3.为null则通过instantiateFragment()方法创建fragment实例
            frag = instantiateFragment(mContext, mManager, className, args)
            //getArguments()：返回实例化fragment时提供的参数
            frag.arguments = args
            //            if (!frag.isAdded) {
            ft.add(mContainerId, frag, tag)
            //  }
        }

        ft.setPrimaryNavigationFragment(frag)

        @IdRes val destId = destination.id

        /**
         * 反射获取mBackStack
         */
        val mBackStack: ArrayDeque<Int>

        val field = FragmentNavigator::class.java.getDeclaredField("mBackStack")
        field.isAccessible = true
        mBackStack = field.get(this) as ArrayDeque<Int>

        val initialNavigation = mBackStack.isEmpty()
        // TODO Build first class singleTop behavior for fragments
        val isSingleTopReplacement = (navOptions != null && !initialNavigation
                && navOptions.shouldLaunchSingleTop()
                && mBackStack.peekLast() == destId)

        val isAdded: Boolean
        if (initialNavigation) {
            isAdded = true
        } else if (isSingleTopReplacement) {
            //singleTop means we only want one instance on the back stack
            if (mBackStack.size > 1) {
                //If the Fragment to be replaced is on the FragmentManager's back stack,
                //a simple replace() isn't enough so we remove it from the back stack
                // and put our replacement on the back stack in its place
                mManager.popBackStack(
                    generateBackStackName(mBackStack.size, mBackStack.peekLast()),
                    FragmentManager.POP_BACK_STACK_INCLUSIVE
                )
                ft.addToBackStack(generateBackStackName(mBackStack.size, destId))
            }
            isAdded = false
        } else {
            ft.addToBackStack(generateBackStackName(mBackStack.size + 1, destId))
            isAdded = true
        }
        if (navigatorExtras is Extras) {
            val extras = navigatorExtras as Extras?
            for ((key, value) in extras!!.sharedElements) {
                ft.addSharedElement(key, value)
            }
        }
        //4.创建的实例添加在事务中
        ft.setReorderingAllowed(true)
        ft.commit()
        //The commit succeeded,update our view of the world
        return if (isAdded) {
            mBackStack.add(destId)
            destination
        } else {
            null
        }
    }

    /**
     * 在父类是private的，直接定义一个方法即可
     */
    private fun generateBackStackName(backIndex: Int, destId: Int?): String {
        return "$backIndex-$destId"
    }
}