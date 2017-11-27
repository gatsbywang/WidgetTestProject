package com.demo.widget.sudoku

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

/**
 * Created by 花歹 on 2017/11/21.
 * Email:   gatsbywang@126.com
 * Description:
 * Thought:
 */
class LockPatternView : View {

    //是否初始化，确保只初始化一次
    private var mIsInit = false

    // 外圆的半径
    private var mDotRadius = 0

    //二维数组初始化，int[3][3]
    private var mPoints: Array<Array<Point?>> = Array(3) {
        Array<Point?>(3, { null })
    }


    // 画笔
    //lateinit  代表为空，跟private  var mLinePaint: Paint? = null 一样，
    // 但是你强调用的时候，如mLinePaint.xxx这时候你需要添加mLinePaint!!.xxx
    // 来说明mLinePaint为null的时候跳过调用
    private lateinit var mLinePaint: Paint
    private lateinit var mPressedPaint: Paint
    private lateinit var mErrorPaint: Paint
    private lateinit var mNormalPaint: Paint
    private lateinit var mArrowPaint: Paint
    // 颜色
    private val mOuterPressedColor = 0xff8cbad8.toInt()
    private val mInnerPressedColor = 0xff0596f6.toInt()
    private val mOuterNormalColor = 0xffd9d9d9.toInt()
    private val mInnerNormalColor = 0xff929292.toInt()
    private val mOuterErrorColor = 0xff901032.toInt()
    private val mInnerErrorColor = 0xffea0945.toInt()


    //按下的时候是否在一个点上
    private var mIsTouchPoint = false

    //选中的点
    private var mSelectPoints = ArrayList<Point>()

    constructor(context: Context) : this(context, null) {

    }


    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0) {

    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {

    }


    override fun onDraw(canvas: Canvas?) {
        // 初始化九宫格
        if (!mIsInit) {
            initDot()
            initPaint()
            mIsInit = true
        }
        //绘制九个宫格
        drawShow(canvas)
    }

    /**
     * 绘制九宫格显示
     */
    private fun drawShow(canvas: Canvas?) {
        // 代表i 0-2之间
        for (i in 0..2) {
            for (point in mPoints[i]) {

                if (point!!.statusIsNormal()) {
                    //先绘制外圆
                    mNormalPaint.color = mOuterNormalColor
                    canvas!!.drawCircle(point!!.centerX.toFloat(), point.centerY.toFloat(), mDotRadius.toFloat(), mNormalPaint)

                    //后绘制内圆
                    mNormalPaint.color = mInnerNormalColor
                    canvas.drawCircle(point!!.centerX.toFloat(), point.centerY.toFloat(), mDotRadius / 6.toFloat(), mNormalPaint)
                }

                if (point!!.statusIsPressed()) {
                    //先绘制外圆
                    mPressedPaint.color = mOuterPressedColor
                    canvas!!.drawCircle(point!!.centerX.toFloat(), point.centerY.toFloat(), mDotRadius.toFloat(), mPressedPaint)

                    //后绘制内圆
                    mPressedPaint.color = mInnerPressedColor
                    canvas.drawCircle(point!!.centerX.toFloat(), point.centerY.toFloat(), mDotRadius / 6.toFloat(), mPressedPaint)
                }

                if (point!!.statusIsError()) {
                    //先绘制外圆
                    mErrorPaint.color = mOuterErrorColor
                    canvas!!.drawCircle(point!!.centerX.toFloat(), point.centerY.toFloat(), mDotRadius.toFloat(), mErrorPaint)

                    //后绘制内圆
                    mErrorPaint.color = mInnerErrorColor
                    canvas.drawCircle(point!!.centerX.toFloat(), point.centerY.toFloat(), mDotRadius / 6.toFloat(), mErrorPaint)
                }
            }
        }


        //绘制两个点直接的连线以及箭头
        drawLine(canvas)
    }

    /**
     * 绘制两个点直接的连线以及箭头
     */
    private fun drawLine(canvas: Canvas?) {
        if (mSelectPoints.size >= 1) {
            //两个点之间需要绘制一条线和箭头
            var lastPoint = mSelectPoints[0]
            for (index in 1..mSelectPoints.size-1) {
                //两个点直接绘制一条线
                drawLine(lastPoint,mSelectPoints[index],canvas,mLinePaint)
                //两个点之间绘制一个箭头
                drawArrow(canvas,lastPoint,mSelectPoints[index],mDotRadius/4.toFloat(),38,mArrowPaint)
                lastPoint = mSelectPoints[index]
            }

            //绘制最后一个点到手指当前位置的连线,如果手指在内圆不要绘制
            if(!checkInRound(lastPoint.centerX.toFloat(),lastPoint.centerY.toFloat(),mMovingX,mMovingY,mDotRadius/4.toFloat())) {
                drawLine(lastPoint, Point(mMovingX.toInt(), mMovingY.toInt(), -1), canvas, mLinePaint)
            }
        }

    }

    /**
     * 画箭头
     */
    private fun drawArrow(canvas: Canvas?,start:Point,end:Point,arrowHeight:Float,angle:Int,paint:Paint){
        //1、首先获取两个点start 和 end 形成的三角直角边
        var dx = end.centerX - start.centerX
        var dy = end.centerY - start.centerY
        //2、获取两个点start 和 end 的距离，也就是斜边
        var d = Math.sqrt(Math.pow(dx.toDouble(), 2.0) + Math.pow(dy.toDouble(), 2.0))
        //3、算出sin cos
        val sin_B = ((end.centerX - start.centerX) / d).toFloat()
        val cos_B = ((end.centerY - start.centerY) / d).toFloat()

        //4、算出3个点的坐标
        val tan_A = Math.tan(Math.toRadians(angle.toDouble())).toFloat()
        val h = (d - arrowHeight.toDouble() - mDotRadius * 1.1).toFloat()
        val l = arrowHeight * tan_A
        val a = l * sin_B
        val b = l * cos_B
        val x0 = h * sin_B
        val y0 = h * cos_B
        val x1 = start.centerX + (h + arrowHeight) * sin_B
        val y1 = start.centerY + (h + arrowHeight) * cos_B
        val x2 = start.centerX + x0 - b
        val y2 = start.centerY.toFloat() + y0 + a
        val x3 = start.centerX.toFloat() + x0 + b
        val y3 = start.centerY + y0 - a
        //绘制路径，将三个点连起来
        val path = Path()
        path.moveTo(x1, y1)
        path.lineTo(x2, y2)
        path.lineTo(x3, y3)
        path.close()
        canvas!!.drawPath(path, paint)

    }

    /**
     * 画线，需要知道两个点的坐标
     * 这线不是从圆心点开始，而是从内圆到内圆画线
     * 利用三角函数计算
     */
    private fun drawLine(start: Point, end: Point, canvas: Canvas?, paint: Paint) {
        //1、首先获取两个点start 和 end 形成的三角直角边
        var dx = end.centerX - start.centerX
        var dy = end.centerY - start.centerY
        //2、获取两个点start 和 end 的距离，也就是斜边
        var pointDistance = Math.sqrt(Math.pow(dx.toDouble(), 2.0) + Math.pow(dy.toDouble(), 2.0))
        //3、得到角度，获取sin，cos值
        var sin = dy.toDouble() / pointDistance
        var cos = dx.toDouble() / pointDistance

        //4、由于我们知道内圆的半径，相当于这是一条斜边，利用上面算得的sin 和 cos，算出圆心与当前point的线起点间的直角边长度
        val rx =(cos*(mDotRadius/6)).toFloat()
        val ry =(sin*(mDotRadius/6)).toFloat()

        //5、可以得出线的两个点坐标
        //讲解下rx ,ry为负数，代表end比start的坐标小，那么start.centerX+rx相当于变小，线是向左延伸，所以没有问题
        canvas!!.drawLine(start.centerX+rx,start.centerY+ry,end.centerX-rx,end.centerY-ry,paint)
    }

    /**
     * 初始化画笔
     * 3个点状态的画笔
     * 线的画笔
     * 箭头的画笔
     */
    private fun initPaint() {
        // 线的画笔
        mLinePaint = Paint()
        mLinePaint.color = mInnerPressedColor
        mLinePaint.style = Paint.Style.STROKE
        mLinePaint.isAntiAlias = true
        mLinePaint.strokeWidth = (mDotRadius / 9).toFloat()
        // 按下的画笔
        mPressedPaint = Paint()
        mPressedPaint.style = Paint.Style.STROKE
        mPressedPaint.isAntiAlias = true
        mPressedPaint.strokeWidth = (mDotRadius / 6).toFloat()
        // 错误的画笔
        mErrorPaint = Paint()
        mErrorPaint.style = Paint.Style.STROKE
        mErrorPaint.isAntiAlias = true
        mErrorPaint.strokeWidth = (mDotRadius / 6).toFloat()
        // 默认的画笔
        mNormalPaint = Paint()
        mNormalPaint.style = Paint.Style.STROKE
        mNormalPaint.isAntiAlias = true
        mNormalPaint.strokeWidth = (mDotRadius / 9).toFloat()
        // 箭头的画笔
        mArrowPaint = Paint()
        mArrowPaint.color = mInnerPressedColor
        mArrowPaint.style = Paint.Style.FILL
        mArrowPaint.isAntiAlias = true
    }

    /**
     * 初始化点
     */
    private fun initDot() {
        //九个点，存到集合  Point[3]
        // 不断绘制的时候这几个点都有状态，而且后面肯定需要回调密码 点肯定是一个对象
        // 计算中心位置
        var width = this.width
        var height = this.height

        // 兼容横竖屏
        var offsetX = 0
        var offsetY = 0
        if (height > width) {
            offsetY = (height - width) / 2
            height = width
        } else {
            offsetX = (width - height) / 2
            width = height
        }
        var squareWidth = width / 3

        mDotRadius = width / 12

        mPoints[0][0] = Point(offsetX + squareWidth / 2, offsetY + squareWidth / 2, 0)
        mPoints[0][1] = Point(offsetX + squareWidth * 3 / 2, offsetY + squareWidth / 2, 1)
        mPoints[0][2] = Point(offsetX + squareWidth * 5 / 2, offsetY + squareWidth / 2, 2)
        mPoints[1][0] = Point(offsetX + squareWidth / 2, offsetY + squareWidth * 3 / 2, 3)
        mPoints[1][1] = Point(offsetX + squareWidth * 3 / 2, offsetY + squareWidth * 3 / 2, 4)
        mPoints[1][2] = Point(offsetX + squareWidth * 5 / 2, offsetY + squareWidth * 3 / 2, 5)
        mPoints[2][0] = Point(offsetX + squareWidth / 2, offsetY + squareWidth * 5 / 2, 6)
        mPoints[2][1] = Point(offsetX + squareWidth * 3 / 2, offsetY + squareWidth * 5 / 2, 7)
        mPoints[2][2] = Point(offsetX + squareWidth * 5 / 2, offsetY + squareWidth * 5 / 2, 8)
    }


    private var mMovingX = 0f

    private var mMovingY = 0f

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        mMovingX = event!!.x
        mMovingY = event!!.y

        when (event!!.action) {
            MotionEvent.ACTION_DOWN -> {
                //判断手指是不是按在一个 宫格上面
                var point = point
                if (point != null) {
                    mIsTouchPoint = true
                    mSelectPoints.add(point)
                    //改变当前点的状态
                    point.setStatusPressed()
                }
            }

            MotionEvent.ACTION_MOVE -> {
                if (mIsTouchPoint) {
                    //按下的时候一定要在一个点上，后面move事件触摸到点上才去改变点的状态
                    var point = point
                    if (point != null) {
                        if (mSelectPoints.contains(point)) {
                            mSelectPoints.add(point)
                        }
                        //改变当前点的状态
                        point.setStatusPressed()
                    }
                }
            }

            MotionEvent.ACTION_UP -> {
                mIsTouchPoint = false
                //回调密码监听


            }


        }
        invalidate()
        return true
    }


    /**
     * 获取按下的点
     *
     */
    private val point: Point?
        get() {
            for (i in mPoints.indices) {
                for (point in mPoints[i]) {
                    //for循环九个点，判断手指位置是否在九个点里面
                    if (checkInRound(point!!.centerX.toFloat(), point.centerY.toFloat(), mMovingX, mMovingY, mDotRadius.toFloat())) {
                        return point
                    }
                }
            }
            return null
        }

    /**
     * 检测是否在指定的点里
     * 利用中心点与按下的点进行计算，两点的距离
     */
    private fun checkInRound(srcPointX: Float, srcPointY: Float, destPointX: Float, destPointY: Float, maxValue: Float): Boolean {

        return Math.sqrt(Math.pow((srcPointX - destPointX).toDouble(), 2.0) + Math.pow((srcPointY - destPointY).toDouble(), 2.0)) < maxValue;

    }

    /**
     * 九宫格的类
     */
    class Point(var centerX: Int, var centerY: Int, var index: Int) {
        private val STATUS_NORMAL = 1
        private val STATUS_PRESSED = 2
        private val STATUS_ERROR = 3

        //当前点的状态，有三种状态
        private var status = STATUS_NORMAL;

        fun setStatusPressed() {
            status = STATUS_PRESSED
        }

        fun setStatusNormal() {
            status = STATUS_NORMAL
        }

        fun setStatusError() {
            status = STATUS_ERROR
        }

        fun statusIsPressed(): Boolean {
            return status == STATUS_PRESSED
        }

        fun statusIsNormal(): Boolean {
            return status == STATUS_NORMAL
        }

        fun statusIsError(): Boolean {
            return status == STATUS_ERROR
        }
    }

}