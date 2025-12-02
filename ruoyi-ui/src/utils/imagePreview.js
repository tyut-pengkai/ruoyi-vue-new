import { api as viewerApi } from "v-viewer"

/**
 * 图片预览功能
 * @param {{url: string,[alt]: string}[]} imageSource 预览的图片数组：[{ url: "图片地址", alt: "图片描述（可选）" }]
 * @param initialViewIndex 开始下标
 */
export default function({ imageSource, initialViewIndex = 0 }) {
  viewerApi({
    options: {
      toolbar: true,
      url: "url",
      initialViewIndex: initialViewIndex
    },
    images: imageSource
  })
}
