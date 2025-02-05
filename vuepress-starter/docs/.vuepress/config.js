import {
    defaultTheme
} from '@vuepress/theme-default'
import {
    searchPlugin
} from '@vuepress/plugin-search'

export default {
    title: "红叶网络验证与软件管理系统",
    markdown: {
        toc: {
            level: [2, 3],
        }
    },
    theme: defaultTheme({
        // Public 文件路径
        logo: '/images/favico.png',
        notFound: ["红叶温馨提示：这里什么都没有哦"],
        backToHome: "去首页看看吧",
        tip: "提示",
        warning: "警告",
        danger: "注意",
        navbar: [{
                text: '首页',
                link: '/',
            },
            {
                text: '演示网站',
                link: 'http://demo.coordsoft.com',
            },
            {
                text: '交流论坛',
                link: 'http://bbs.coordsoft.com',
            },
            {
                text: '购买授权',
                link: 'http://shop.coordsoft.com',
            }, {
                text: 'QQ交流群',
                children: [
                    {
                        text: '加入群聊①',
                        link: 'https://jq.qq.com/?_wv=1027&k=tT0T695Q',
                    },
                    {
                        text: '加入群聊②',
                        link: 'https://qm.qq.com/q/kiEZTm53Jm',
                    }
                ]
            }
            // // NavbarItem
            // {
            //     text: 'Foo',
            //     link: '/foo/',
            // },
            // // NavbarGroup
            // {
            //     text: 'Group',
            //     children: ['/group/foo.md', '/group/bar.md'],
            // },
            // // 字符串 - 页面文件路径
            // '/bar/README.md',
        ],
        // 侧边栏数组
        // 所有页面会使用相同的侧边栏
        sidebar: [{
                text: "系统介绍",
                collapsible: false,
                children: [{
                    text: '系统简介',
                    collapsible: true,
                    link: '/introduce/introduce/introduce.md'
                }, {
                    text: '主要功能',
                    collapsible: true,
                    link: '/introduce/function/function.md'
                }, {
                    text: '演示站点',
                    collapsible: true,
                    link: '/introduce/demo/demo.md'
                }, '/introduce/update-log/update-log.md', ]
            }, {
                text: '部署搭建',
                collapsible: false,
                children: [{
                    text: '需求环境',
                    collapsible: true,
                    link: '/install/require-env/require-env.md'
                }, {
                    text: '部署教程',
                    collapsible: true,
                    children: [{
                            text: 'Linux服务器',
                            collapsible: true,
                            children: ['/install/linux/bt/linux-bt.md']
                        },
                        {
                            text: 'Windows服务器',
                            collapsible: true,
                            children: [
                                '/install/windows/bt/windows-bt.md',
                                '/install/windows/upupw/windows-upupw.md'
                            ]
                        },
                    ]
                }, {
                    text: '激活授权',
                    collapsible: true,
                    link: '/install/license/license.md'
                }
                // , {
                //     text: '初始配置',
                //     collapsible: true,
                //     link: '/install/init-config/init-config.md'
                // }
                ],
            },
            {
                text: '使用指南',
                collapsible: false,
                children: [
                    '/guide/getting-started/getting-started.md',
                    // '/guide/quick-access/quick-access.md',
                    {
                        text: '代码接入',
                        collapsible: true,
                        children: [
                            '/guide/code-access/code-access.md',
                            '/guide/code-access/api-docs.md',
                        ]
                    },
                    {
                        text: '功能介绍',
                        collapsible: true,
                        children: [
                            '/guide/function-introduce/software/software.md',
                            '/guide/function-introduce/version/version.md',
                            '/guide/function-introduce/template/template.md',
                            '/guide/function-introduce/card/card.md',
                            '/guide/function-introduce/app_user/app_user.md',
                            '/guide/function-introduce/user_center/user_center.md',
                            '/guide/function-introduce/device/device.md',
                            '/guide/function-introduce/online/online.md',
                            '/guide/function-introduce/batch/batch.md',
                        ]
                    },
                    {
                        text: '支付对接',
                        collapsible: true,
                        children: [
                            '/guide/payment/alipay-f2f/alipay-f2f.md',
                            '/guide/payment/wechatpay-native/wechatpay-native.md',
                            '/guide/payment/epay/epay.md',
                        ]
                    },
                    '/guide/qa/qa.md',
                ],
            },
            // 字符串 - 页面文件路径
            // '/bar/README.md',
        ],
    }),
    plugins: [
        searchPlugin({
            // 配置项
        }),
    ],
}