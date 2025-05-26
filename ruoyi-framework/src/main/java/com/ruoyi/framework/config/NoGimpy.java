package com.ruoyi.framework.config;

import com.google.code.kaptcha.GimpyEngine;
import com.google.code.kaptcha.util.Configurable;

import java.awt.image.BufferedImage;

/**
 * @author liangyq
 */
public class NoGimpy extends Configurable implements GimpyEngine {

    @Override
    public BufferedImage getDistortedImage(BufferedImage baseImage) {
        return baseImage;
    }

}
