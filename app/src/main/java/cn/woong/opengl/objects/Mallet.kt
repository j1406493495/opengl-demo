package cn.woong.opengl.objects

import cn.woong.opengl.constants.Constants

/**
 * @author Woong on 2019/6/3
 * @website http://woong.cn
 */
class Mallet() {
    companion object {
        private val POSITION_COMPONENT_COUNT = 2
        private val COLOR_COMPONENT_COUNT = 3
        private val STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * Constants.BYTES_PER_FLOAT

    }
}