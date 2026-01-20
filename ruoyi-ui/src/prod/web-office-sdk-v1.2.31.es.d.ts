interface IDisposable {
    dispose(): void;
}
declare class DisposableStore implements IDisposable {
    static DISABLE_DISPOSED_WARNING: boolean;
    private readonly _toDispose;
    private _isDisposed;
    dispose(): void;
    /**
     * @returns `true` if this object has been disposed of.
     */
    get isDisposed(): boolean;
    /**
     * Dispose of all registered disposables but do not mark this object as disposed.
     */
    clear(): void;
    add<T extends IDisposable>(o: T): T;
    delete<T extends IDisposable>(o: T): void;
    deleteAndLeak<T extends IDisposable>(o: T): void;
}
declare abstract class Disposable$1 implements IDisposable {
    protected readonly _store: DisposableStore;
    dispose(): void;
    protected register<T extends IDisposable>(o: T): T;
}
type Constructor<T = Record<string, any>> = new (...args: any[]) => T;
interface IDisposableRegister {
    dispose(): void;
    register<T extends IDisposable>(t: T): T;
}

declare type EventType = string | symbol;
declare type Handler<T = unknown> = (event: T) => void;
declare type WildcardHandler<T = Record<string, unknown>> = (type: keyof T, event: T[keyof T]) => void;
declare type EventHandlerList<T = unknown> = Array<Handler<T>>;
declare type WildCardEventHandlerList<T = Record<string, unknown>> = Array<WildcardHandler<T>>;
declare type EventHandlerMap<Events extends Record<EventType, unknown>> = Map<keyof Events | '*', EventHandlerList<Events[keyof Events]> | WildCardEventHandlerList<Events>>;
interface Emitter<Events extends Record<EventType, unknown>> {
    all: EventHandlerMap<Events>;
    on<Key extends keyof Events>(type: Key, handler: Handler<Events[Key]>): void;
    on(type: '*', handler: WildcardHandler<Events>): void;
    off<Key extends keyof Events>(type: Key, handler?: Handler<Events[Key]>): void;
    off(type: '*', handler: WildcardHandler<Events>): void;
    emit<Key extends keyof Events>(type: Key, event: Events[Key]): void;
    emit<Key extends keyof Events>(type: undefined extends Events[Key] ? Key : never): void;
}

interface Command<Result = void, Params = undefined> {
    name: string;
    execute: (params: Params) => Promise<Result>;
}
interface API {
    name: string;
    run: (params: any) => any;
}
declare class WebOfficeEvent<T = unknown> {
    messageType: string;
    data: T;
    constructor(event: {
        messageType: string;
        data: T;
    });
}

type Events = Record<string | symbol, WebOfficeEvent>;
type Once = (type: string, handler: Handler<WebOfficeEvent>) => void;
declare const IEventEmitter: unique symbol;
type IEventEmitter = Emitter<Events> & {
    once: Once;
} & IDisposable;

type HeaderType = Record<string, string | number | boolean>;
declare class Rpc {
    private targetWindow;
    private id;
    private targetId;
    private apiListener;
    private requestListener;
    private lastCommunicateTime;
    private timeout;
    constructor(targetWindow: Window, options?: {
        targetId?: string;
        selfId?: string;
        timeout?: number;
    });
    call<T = unknown>(api: string, params: unknown): Promise<[null, Error] | [T, null]>;
    private handler;
    isConnected(options?: {
        retryTime?: number;
    }): Promise<boolean>;
    closeServer(): void;
    addApi(api: API): void;
    getApi(apiName: string): API | undefined;
    clearApi(): void;
    private headers;
    setHeaders(headerObj: HeaderType): this;
    dispose(): void;
    private createRequestData;
    private isRequestData;
    private isValidResponse;
    private isValidRequest;
    private getListenerKey;
    private debugInfo;
}

interface CommandParams {
    callStack: string;
    method: 'GET' | 'SET' | 'APPLY';
    applyParams?: unknown[];
    setValue?: unknown;
    callStackMapKey?: string;
}
declare class ExecuteApiCommand implements Command<unknown, CommandParams> {
    private rpc;
    private proxyFactory;
    name: string;
    constructor(rpc: Rpc, proxyFactory: IProxyFactory);
    execute(commandParams: CommandParams): Promise<unknown>;
}

interface IProxyFactory {
    createProxy(obj: unknown, callStackMapKey?: string, curKey?: string): unknown;
    setCommand(executeApiCommand: ExecuteApiCommand): void;
}
/**
 * API 对象的代理工厂类
 * 将对 API 对象进行遍历克隆
 */
declare class ProxyFactory implements IProxyFactory {
    private executeApiCommand;
    setCommand(executeApiCommand: ExecuteApiCommand): void;
    createProxy(obj: unknown, callStackMapKey?: string, curKey?: string): unknown;
    private createSpecialProxy;
    private createNode;
    private isNotFirstLevelFromRoot;
}

declare class StateService {
    ApiEvent: IEventEmitter;
    private rpc;
    documentVersion: string;
    private applicationReadyBarrier;
    private applicationServerReadyBarrier;
    constructor(ApiEvent: IEventEmitter, rpc: Rpc);
    whenApplicationServerReady(options?: {
        needConnection?: boolean;
        retryTime?: number;
    }): Promise<void>;
    whenApplicationReady(options?: {
        needConnection?: boolean;
        retryTime?: number;
    }): Promise<void>;
    reset(): void;
    private init;
}

declare class GetApplicationCommand extends Disposable$1 implements Command<unknown> {
    private rpc;
    private proxyFactory;
    private stateService;
    name: string;
    constructor(rpc: Rpc, proxyFactory: ProxyFactory, stateService: StateService);
    execute(): Promise<unknown>;
    private setNotReadyTimeoutLog;
}

interface WebOfficeApiResponse<Result> {
    retcode: number;
    msg: string;
    result?: Result;
}

type MemberDecorator = <T>(target: Target, propertyKey: PropertyKey, descriptor?: TypedPropertyDescriptor<T>) => TypedPropertyDescriptor<T> | void;
type MetadataKey = string | symbol;
type PropertyKey = string | symbol;
type Target = object | Function;
declare function decorate(decorators: ClassDecorator[], target: Function): Function;
declare function decorate(decorators: MemberDecorator[], target: object, propertyKey?: PropertyKey, attributes?: PropertyDescriptor): PropertyDescriptor | undefined;
declare function metadata<MetadataValue>(metadataKey: MetadataKey, metadataValue: MetadataValue): (target: Target, propertyKey?: PropertyKey) => void;
declare function getMetadata<MetadataValue>(metadataKey: MetadataKey, target: Target, propertyKey?: PropertyKey): MetadataValue | undefined;
declare function getOwnMetadata<MetadataValue>(metadataKey: MetadataKey, target: Target, propertyKey?: PropertyKey): MetadataValue | undefined;
declare function hasOwnMetadata(metadataKey: MetadataKey, target: Target, propertyKey?: PropertyKey): boolean;
declare function hasMetadata(metadataKey: MetadataKey, target: Target, propertyKey?: PropertyKey): boolean;
declare function defineMetadata<MetadataValue>(metadataKey: MetadataKey, metadataValue: MetadataValue, target: Target, propertyKey?: PropertyKey): void;
declare const Reflection: {
    decorate: typeof decorate;
    defineMetadata: typeof defineMetadata;
    getMetadata: typeof getMetadata;
    getOwnMetadata: typeof getOwnMetadata;
    hasMetadata: typeof hasMetadata;
    hasOwnMetadata: typeof hasOwnMetadata;
    metadata: typeof metadata;
};
declare global {
    namespace Reflect {
        let decorate: typeof Reflection.decorate;
        let defineMetadata: typeof Reflection.defineMetadata;
        let getMetadata: typeof Reflection.getMetadata;
        let getOwnMetadata: typeof Reflection.getOwnMetadata;
        let hasOwnMetadata: typeof Reflection.hasOwnMetadata;
        let hasMetadata: typeof Reflection.hasMetadata;
        let metadata: typeof Reflection.metadata;
    }
}

/** Constructor type */
declare type constructor<T> = {
    new (...args: any[]): T;
};

declare class DelayedConstructor<T> {
    private wrap;
    private reflectMethods;
    constructor(wrap: () => constructor<T>);
    createProxy(createObject: (ctor: constructor<T>) => T): T;
    private createHandler;
}

interface ClassProvider<T> {
    useClass: constructor<T> | DelayedConstructor<T>;
}

interface ValueProvider<T> {
    useValue: T;
}

declare type InjectionToken<T = any> = constructor<T> | string | symbol | DelayedConstructor<T>;

interface TokenProvider<T> {
    useToken: InjectionToken<T>;
}

/**
 * Provide a dependency using a factory.
 * Unlike the other providers, this does not support instance caching. If
 * you need instance caching, your factory method must implement it.
 */
interface FactoryProvider<T> {
    useFactory: (dependencyContainer: DependencyContainer) => T;
}

declare enum Lifecycle {
    Transient = 0,
    Singleton = 1,
    ResolutionScoped = 2,
    ContainerScoped = 3
}

declare type RegistrationOptions = {
    /**
     * Customize the lifecycle of the registration
     * See https://github.com/microsoft/tsyringe#available-scopes for more information
     */
    lifecycle: Lifecycle;
};

interface Disposable {
    dispose(): Promise<void> | void;
}

declare type Frequency = "Always" | "Once";

declare type InterceptorOptions = {
    frequency: Frequency;
};

declare type ResolutionType = "Single" | "All";
interface PreResolutionInterceptorCallback<T = any> {
    /**
     * @param token The InjectionToken that was intercepted
     * @param resolutionType The type of resolve that was called (i.e. All or Single)
     */
    (token: InjectionToken<T>, resolutionType: ResolutionType): void;
}
interface PostResolutionInterceptorCallback<T = any> {
    /**
     * @param token The InjectionToken that was intercepted
     * @param result The object that was resolved from the container
     * @param resolutionType The type of resolve that was called (i.e. All or Single)
     */
    (token: InjectionToken<T>, result: T | T[], resolutionType: ResolutionType): void;
}
interface DependencyContainer extends Disposable {
    register<T>(token: InjectionToken<T>, provider: ValueProvider<T>): DependencyContainer;
    register<T>(token: InjectionToken<T>, provider: FactoryProvider<T>): DependencyContainer;
    register<T>(token: InjectionToken<T>, provider: TokenProvider<T>, options?: RegistrationOptions): DependencyContainer;
    register<T>(token: InjectionToken<T>, provider: ClassProvider<T>, options?: RegistrationOptions): DependencyContainer;
    register<T>(token: InjectionToken<T>, provider: constructor<T>, options?: RegistrationOptions): DependencyContainer;
    registerSingleton<T>(from: InjectionToken<T>, to: InjectionToken<T>): DependencyContainer;
    registerSingleton<T>(token: constructor<T>): DependencyContainer;
    registerType<T>(from: InjectionToken<T>, to: InjectionToken<T>): DependencyContainer;
    registerInstance<T>(token: InjectionToken<T>, instance: T): DependencyContainer;
    /**
     * Resolve a token into an instance
     *
     * @param token The dependency token
     * @return An instance of the dependency
     */
    resolve<T>(token: InjectionToken<T>): T;
    resolveAll<T>(token: InjectionToken<T>): T[];
    /**
     * Check if the given dependency is registered
     *
     * @param token The token to check
     * @param recursive Should parent containers be checked?
     * @return Whether or not the token is registered
     */
    isRegistered<T>(token: InjectionToken<T>, recursive?: boolean): boolean;
    /**
     * Clears all registered tokens
     */
    reset(): void;
    clearInstances(): void;
    createChildContainer(): DependencyContainer;
    /**
     * Registers a callback that is called when a specific injection token is resolved
     * @param token The token to intercept
     * @param callback The callback that is called before the token is resolved
     * @param options Options for under what circumstances the callback will be called
     */
    beforeResolution<T>(token: InjectionToken<T>, callback: PreResolutionInterceptorCallback<T>, options?: InterceptorOptions): void;
    /**
     * Registers a callback that is called after a successful resolution of the token
     * @param token The token to intercept
     * @param callback The callback that is called after the token is resolved
     * @param options Options for under what circumstances the callback will be called
     */
    afterResolution<T>(token: InjectionToken<T>, callback: PostResolutionInterceptorCallback<T>, options?: InterceptorOptions): void;
    /**
     * Calls `.dispose()` on all disposable instances created by the container.
     * After calling this, the container may no longer be used.
     */
    dispose(): Promise<void> | void;
}

type IContainer = DependencyContainer;
declare const IContainer: unique symbol;

interface LibraryMap {
    'TENCENT_DOCS_LIBRARY__doc_weixin_qq_com_assets_v11': string;
    'TENCENT_DOCS_LIBRARY__doc_weixin_qq_com_scenario_wlwk': string;
    'TENCENT_DOCS_LIBRARY__docs_gtimg_com_assets': string;
    'TENCENT_DOCS_LIBRARY__docs_qq_com_assets': string;
    'TENCENT_DOCS_LIBRARY__docs_qq_com_assets_v1': string;
    'TENCENT_DOCS_LIBRARY__docs_qqapi': string;
    'TENCENT_DOCS_LIBRARY__docs_qqapi_v1': string;
    'TENCENT_DOCS_LIBRARY__ie_version_report': string;
    'TENCENT_DOCS_LIBRARY__jquery': string;
    'TENCENT_DOCS_LIBRARY__jweixin_1_6_0': string;
    'TENCENT_DOCS_LIBRARY__jwxwork_1_0_0': string;
    'TENCENT_DOCS_LIBRARY__preload': string;
    'TENCENT_DOCS_LIBRARY__qqjssdk_1_0_0': string;
    'TENCENT_DOCS_LIBRARY__raven': string;
    'TENCENT_DOCS_LIBRARY__react': string;
    'TENCENT_DOCS_LIBRARY__scenario_wl_v3': string;
    'TENCENT_DOCS_LIBRARY__set_map_polyfill': string;
    'TENCENT_DOCS_LIBRARY__socket_io': string;
    'TENCENT_DOCS_LIBRARY__test': string;
    'TENCENT_DOCS_LIBRARY__weixin_discussion_ext': string;
    'TENCENT_DOCS_LIBRARY__wemeet': string;
    'TENCENT_DOCS_LIBRARY__weblog': string;
    'TENCENT_DOCS_LIBRARY__react_16_13_1': string;
    'TENCENT_DOCS_LIBRARY__abt_sdk': string;
    'TENCENT_DOCS_LIBRARY__aegis': string;
    'TENCENT_DOCS_LIBRARY__alloyteam_tdocs_jsapi_1_6_0': string;
    'TENCENT_DOCS_LIBRARY__weblog_proxy': string;
    'TENCENT_DOCS_LIBRARY__weblog_tdoc_browser': string;
    'TENCENT_DOCS_LIBRARY__wx_qq_com_tdocs_jsapi_latest': string;
    'TENCENT_DOCS_LIBRARY__aegis_v2': string;
    'TENCENT_DOCS_LIBRARY__i18next_21_10_0': string;
    'TENCENT_DOCS_LIBRARY__i18next_21': string;
    'TENCENT_DOCS_LIBRARY__react_18': string;
    'TENCENT_DOCS_LIBRARY__dark_mode_1': string;
    'TENCENT_DOCS_LIBRARY__magicbox_hook': string;
    'TENCENT_DOCS_LIBRARY__tdocs_js_api': string;
    'TENCENT_DOCS_LIBRARY__doc_weixin_qq_com_assets_v11-d28706': string;
    'TENCENT_DOCS_LIBRARY__docs_gtimg_com_assets-f03d24': string;
    'TENCENT_DOCS_LIBRARY__docs_qq_com_assets-cd3817': string;
    'TENCENT_DOCS_LIBRARY__docs_qq_com_assets_v1-cd3817': string;
    'TENCENT_DOCS_LIBRARY__dark_mode': string;
    'TENCENT_DOCS_LIBRARY__web_office_bootstrap': string;
    'TENCENT_DOCS_LIBRARY__dark_mode_lightonly': string;
    'TENCENT_DOCS_LIBRARY__tailwindcss_3_4_16': string;
    'TENCENT_DOCS_LIBRARY__font_awesome_6_4_0': string;
    'TENCENT_DOCS_LIBRARY__WEBFONTS_fa-brands-400.ttf': string;
    'TENCENT_DOCS_LIBRARY__WEBFONTS_fa-brands-400.woff2': string;
    'TENCENT_DOCS_LIBRARY__WEBFONTS_fa-regular-400.ttf': string;
    'TENCENT_DOCS_LIBRARY__WEBFONTS_fa-regular-400.woff2': string;
    'TENCENT_DOCS_LIBRARY__WEBFONTS_fa-solid-900.ttf': string;
    'TENCENT_DOCS_LIBRARY__WEBFONTS_fa-solid-900.woff2': string;
    'TENCENT_DOCS_LIBRARY__WEBFONTS_fa-v4compatibility.ttf': string;
    'TENCENT_DOCS_LIBRARY__WEBFONTS_fa-v4compatibility.woff2': string;
    'TENCENT_DOCS_LIBRARY__chart_4_5_0': string;
}

interface INpmCommonConfig {
    '@tencent/tencent-doc-report': Partial<ITencentDocReport>;
    '@tencent/docs-miniapp-sdk': Partial<IDocMiniappSdk>;
    '@tencent/docs-design-resources': Partial<IDocDesignResources>;
    '@tencent/clipboard': Partial<IClipboard>;
    '@tencent/image-components': Partial<IImageComponent>;
    '@tencent/docs_offline': Partial<IDocOffline>;
    '@tencent/ark-application': Partial<ITencentArkApplication>;
    '@tencent/ark-domain': Partial<ITencentArkDomain>;
    '@tencent/ark-extensions': Partial<ITencentArkExtensions>;
    '@tencent/tdoc-ark': Partial<ITencentTdocArk>;
    '@tencent/netcheck': Partial<ITencentNetcheck>;
    '@tencent/tencent_doc_net': Partial<ITencentDocNet>;
    '@tencent/form-components': Partial<ITencentFormComponents>;
    '@tencent/form-utils': Partial<ITencentFormUtils>;
    '@tencent/docs-opentelemetry': Partial<ITencentDocsOpentelemetry>;
    '@tencent/tdocs-jsapi': Partial<ITencentTdocsJsapi>;
    '@tencent/docs-scenario-components': Partial<ITencentDocsScenarioComponents>;
    '@tencent/melo-utils': Partial<ITencentMeloUtils>;
    '@tencent/docs-scenario-components-mobile-folder-selector': Partial<ITencentSCSelector>;
    '@tencent/docs-scenario-components-pc-folder-selector': Partial<ITencentSCSelector>;
    '@tencent/docs-titlebar-service': Partial<ITencentSCTitlebarService>;
    '@tencent/docs-login': Partial<ITencentSCLogin>;
    '@tencent/docs-scenario-components-watermark-generate': Partial<ITencentSCWatermarkGenerate>;
    '@tencent/docs-scenario-components-utils': Partial<IScenarioComponentUtils>;
    '@tencent/docs-electron-client': Partial<IDocsElectronClient>;
}
interface ITencentDocsScenarioComponents {
    WECOM_DOMAIN: string;
    DOCS_DOMAIN: string;
    TOCAO_URL: string;
    FONT_URL: string;
    MINI_PROGRAM_URL: string;
    ANDROID_DOWNLOAD_URL: string;
    DOCS_COMPONENT_URL: string;
    FEEDBACK_FOR_WEWORK_URL: string;
}
interface ITencentDocReport {
    SDK_BUNDLE_URL: string;
    ENT_SDK_BUNDLE_URL: string;
    docrpWeixinOrigin: string;
    wecomMainHost: string;
    wecomAddonHost: string;
    cookieDomain: string;
    reportIdqqimgOrigin: string;
    reportIdqqimgHost: string;
    reportUrlOrigin: string;
    reportUrlHost: string;
    reportHuatuoOrigin: string;
    beaconOrigin: string;
    tencentDocCdnGoOrigin: string;
    qqwebQQHost: string;
    qunQQHost: string;
    interceptorIgnoreUrls: string[];
}
interface IDocMiniappSdk {
    TOC_ORIGIN: string;
}
interface IDocDesignResources {
    CDN_ORIGIN: string;
}
interface IClipboard {
    CRA_PWA_URL: string;
    SDK_WX_URL: string;
}
interface IImageComponent {
    Qqadapt: string;
    PUB_ORIGIN: string;
    SDK_JS_URL: string;
    wechatSignatureUrl: string;
}
interface IDocOffline {
    TOC_ORIGIN: string;
}
interface ITencentArkApplication {
    TENCENT_DOCS_HOME: string;
    TENCENT_DOCS_EDITOR_REGEXP: string;
    WECOM_DOCS_HOME: string;
    WECOM_DOCS_EDITOR: string;
    FEEDBACK_URL: string;
    EXACTLY_PARENT_TOC_COOKIE_DOMAIN: string;
    PARENT_TOC_COOKIE_DOMAIN: string;
    TOC_COOKIE_DOMAIN: string;
    PARENT_TOB_COOKIE_DOMAIN: string;
    TOB_COOKIE_DOMAIN: string;
    WECOM_HOME_PAGE: string;
    PAGE_JUMP_TO_C: string;
    TOC_HOME_PAGE_REGEXP: string;
    TOB_LOGIN_PAGE_URL: string;
    TOC_HOME_PAGE: string;
    C_JUMP_TO_B_URL: string;
    C_JUMP_TOB_LOGIN_PAGE_URL: string;
    C_LOGIN_URL: string;
    TOB_HOME_PAGE_REGEXP: string;
    TOB_MALL_PAGE_REGEXP: string;
    TOC_MALL_PAGE_REGEXP: string;
    TOC_MALL_PAGE: string;
}
interface ITencentArkDomain {
    WECHAT_WEBVIEW_LOGIN: string;
    LOGIN_PAGE_URL: string;
    TOC_HOST: string;
    TOB_HOST: string;
    DOCS_WW_LOGIN: string;
    OPEN_WEIXIN_AUTHORIZE: string;
    TOB_TARGET_ORIGIN: string;
    TOB_IFRAME_URL_ACCOUNT_LIST: string;
    TOB_IFRAME_URL_SET_COOKIE: string;
    TOC_TARGET_ORIGIN: string;
    TOC_IFRAME_URL_ACCOUNT_LIST: string;
    TOC_IFRAME_URL_SET_COOKIE: string;
}
interface ITencentArkExtensions {
    React: string;
    ReactDOM: string;
    DEFAULT_COMPONENT_CDN_PREFIX: string;
    i18next: string;
}
interface ITencentTdocArk {
    FEEDBACK_URL: string;
}
interface ITencentNetcheck {
    cdnSources: string[];
}
interface ITencentDocNet {
    TOB_HOST: string;
    TOB_HOST_REGEXP: string;
    esLogUrl: string;
    REPORT_URLS: string[];
    WEBSOCKET_URLS: string[];
    OTHER_URLS: string[];
    PRIVATE_REPORT_URLS: string[];
    PRIVATE_ADD_HEADER_LOG_TASK_ID: boolean;
}
interface ITencentFormComponents {
    TRAVELCARD_IMAGES: string[];
}
interface ITencentFormUtils {
    TOC_HOST: string;
    TOB_HOST: string;
    SCENARIO_URL_REGEXP: string;
    QQ_COM_REGEXP: string;
    TDOCS_URL_REGEXP: string;
    WEDRIVE_LINK_REGEXP: string;
    ENT_LINK_REGEXP: string;
    GET_FILE_ID_CGI_URL: string;
    APIS_MAP_GEOLOCATION_URL: string;
    APIS_MAP_GEOCODER_URL: string;
}
interface ITencentDocsOpentelemetry {
    TDOCS_TPS_CONFIG: {
        HOST: string;
        TENANT_ID: string;
        TRACE_URL: string;
    };
    WECOM_TPS_CONFIG: {
        HOST: string;
        TENANT_ID: string;
        TRACE_URL: string;
    };
    IGNORE_URLS_REGEXP: string[];
}
interface ITencentTdocsJsapi {
    TOC_HOST: string;
    TOB_HOST: string;
    TOB_COOKIE_DOMAIN: string;
    COOKIE_DOMAINS: string[];
    QQJSSDK: string;
    WXJSSDK: string;
    WW_MOBILE_JSSDK: string;
    WW_PC_JSSDK: string;
    WW_MOBILE_JSSDK_OLD: string;
    WW_PC_JSSDK_OLD: string;
    WEDRIVE_LINK_REGEXP: string;
    PRIVATE_WEDRIVE_LINK_REGEXP: string;
    WEDOC_FORM_LINK_REGEXP: string;
    TOC_ORIGIN_REGEXP: string;
    TOB_ORIGIN_REGEXP: string;
    APP_WORK_WEIXIN_ADMIN_URL_REGEXP: string;
    TOC_SUPPORT_URL: string;
    TOB_SUPPORT_URL: string;
}
interface ITencentMeloUtils {
    TOB_HOST: string;
}
interface ITencentSCSelector {
    BUSINESS_SPACE: string;
    USE_SHARE_SPACE_TITLE: boolean;
}
interface ITencentSCTitlebarService {
    SCAN_PRIVATE_EXTENSION: boolean;
    WEB_LOG_EXPORT_TO_FILE: boolean;
    SHOW_C_HOME_STYLE: boolean;
}
interface ITencentSCLogin {
    USE_TDOCS_UID: boolean;
    DO_EID_LOGIN: boolean;
}
interface ITencentSCWatermarkGenerate {
    USE_NEED_SHOW_VISITOR: boolean;
}
interface IScenarioComponentUtils {
    DISABLE_VIP: boolean;
}
interface IDocsElectronClient {
    /** 禁用桌面端广告与引流 */
    DISABLE_ELECTRON_AD: boolean;
}

/**
 * 多品类公用的配置声明
 */

declare const PlatformList: readonly ["pc", "mobile", "pad"];
/**
 * 客户端平台类型
 */
type Platform = (typeof PlatformList)[number];
/**
 * 全品类公共配置项
 */
interface ICommonConfig {
    /**
     * @deprecated 现在用 tenantName 了
     * 租户, 应该由流量层服务动态注入, 不需要在 CAC 中显式配置
     */
    tenant: string;
    /**
     * 用户域, 生成配置会时会自动注入
     */
    domain: string;
    /**
     * 接入方, 生成配置时会自动注入
     */
    client: string;
    /**
     * 客户端平台类型
     */
    platform: Platform;
    /**
     * 右键菜单栏
     */
    contextMenu: Partial<IContextMenu> | false;
    /**
     * 中间编辑区的组件，只要不包含在toolbar，titlebar 都可以放在这里
     */
    widget: Partial<IWidget> | false;
    /**
     * 标题栏
     */
    titlebar: Partial<ITitlebar> | false;
    /**
     * titlebar 下菜单栏
     */
    menu: Partial<IMenu>;
    /**
     * 工具栏
     */
    toolbar: Partial<IToolbar> | false;
    /**
     * 移动端的胶囊 bar
     */
    actionButton: Partial<IActionButton> | false;
    /**
     * 运行时环境相关配置，一般和部署目标有关。如应用域名、lib 资源 url 等
     */
    runtime: Partial<IRuntime>;
    /**
     * 跳转的外链，比如帮助中心、举报等
     */
    links: Partial<ILinks>;
    /**
     * 公共资源地址，如 favicon 等
     */
    assets: Partial<IAssets>;
    /**
     * 各种 npm 包的配置项
     */
    npm: Partial<INpmCommonConfig>;
    /**
     * 品类入口配置
     *
     * 该配置项是一个 Map, key 是 `@tencent/docs-mime-types` 中的 [`PadType`](https://git.woa.com/tencent-doc/docs-monorepo/blob/master/packages/docs-mime-types/src/constant/types.ts#L215),
     * value 是对于该品类的具体配置项. 所有可配置项默认均为 false.
     */
    docEntries: Partial<IDocEntriesConfig>;
    /**
     * 品类自定义配置项
     */
    custom: Partial<ICustom$1>;
    /**
     * @deprecated 已经拍平到 ICommonConfig
     * @ yiderzhang
     * DWS 动态从环境变量中注入，租户信息
     */
    tenantConfig: Partial<ITenantConfig>;
    /**
     * 租户名
     */
    tenantName: string;
    /**
     * 租户文档名
     */
    tenantDocName: string;
    /**
     * 租户相关 logo 信息
     */
    tenantLogo: {
        /**
         * 黑色 logo svg 地址
         */
        SVGBlack: string;
        /**
         * 黑色 Solid 风格 logo svg 地址
         */
        SVGSolidBlack: string;
        /**
         * 彩色 logo svg 地址
         */
        SVGColor: string;
    };
}
/**
 * 运行时环境配置
 */
interface IRuntime {
    domainName: string;
    apiRoot: string;
    /**
     * 支持域名子路径
     * 其取值默认为 '', 如果 domainName 为 docs.qq.com/abc, 则 prefix 为 /abc
     */
    prefix: string;
    staticPublicPath: string;
    cdnServer: {
        preferred: ICdnInfo;
        alternate: ICdnInfo;
    } & Partial<{
        homeDomain: ICdnInfo;
    }>;
    domainNameAlias: string[];
    /**
     * 外部库 library 资源的 publicPath
     * 为了适配在 ejs 中使用 library, 在构建时就替换为具体路径, 私有化需要设置为相对路径
     */
    libPublicPath: string;
    componentCdnPrefix: string;
    reportCgiMap: Partial<{
        /**
         * @deprecated
         * 已经废弃, 2025.11.18, 建议品类移除调用
         */
        zzsReport: string;
        monitorReport: string;
        tdwReport: string;
    }>;
    library: Partial<LibraryMap>;
    imageHosting: {
        domainSuffix: string[];
    };
}
interface ICdnInfo {
    domain: string;
    path: string;
}
/**
 * 跳转的外链
 */
interface ILinks {
    index: string;
    home: string;
    helpCenter: string;
    feedback: string;
    feedbackForApp: string;
    report: string;
    privacyPolicy: string;
    thirdPartySharedInfo: string;
    thirdPartyAuthManage: string;
    personalInfoCollectionList: string;
    infringementGuidelines: string;
    featureDescription: string;
    communityConvention: string;
    serviceAgreement: string;
    qqH5ToMiniprogram: string;
    appealPage: string;
    qbWenKuUrl: string;
    weixinOpenAuth: string;
    certificationQA: string;
    docsComponentMiddleware: string;
    aiSeeFeedback: string;
}
/**
 * 公共资源地址，如 favicon 等
 */
interface IAssets {
    favicon: string;
    errorImagePlaceholder: string;
}
/**
 * 工具栏
 */
interface IToolbar {
    toolbar: boolean;
    slide: boolean;
    /**
     * rainbow 按钮，会员专享, 会员功能入口集合
     */
    vipOnly: boolean;
}
/**
 * 右键菜单栏
 */
interface IContextMenu {
    insertImageButton: boolean;
    pasteSpecialCombine: boolean;
    rangeAuthButtonGroup: boolean;
    /**
     * 插入菜单
     */
    insert: boolean;
}
/**
 * 三条杠菜单
 */
interface IMenu {
    /**
     * 文档创建者信息
     */
    docCreateInfo: boolean;
    /**
     * 新建
     */
    new: boolean;
    /**
     * 复制链接
     */
    copyLink: boolean;
    /**
     * 导出为
     */
    exportAs: boolean;
    /**
     * 生成副本
     */
    clone: boolean;
    /**
     * 另存为我的模版
     */
    saveTemplate: boolean;
    /**
     * 修订记录
     */
    editHistory: boolean;
    /**
     * 浏览记录
     */
    visitHistory: boolean;
    /**
     * 设置权限
     */
    setAuth: boolean;
    /**
     * 申请权限
     */
    applyAuth: boolean;
    /**
     * 开启无障碍读屏
     */
    aria: boolean;
    /**
     * 帮助与反馈
     */
    helpCenter: boolean;
    /**
     * 发送到电脑
     */
    sendToPc: boolean;
    /**
     * 移动到
     */
    moveTo: boolean;
    /**
     * 重命名
     */
    rename: boolean;
    /**
     * 查找替换
     */
    findAndReplace: boolean;
    /**
     * 使用客户端打开
     */
    openClient: boolean;
    /**
     * 导出为pdf
     */
    canExportPdf: boolean;
    /**
     * 点赞
     */
    canLike: boolean;
    /**
     * 工具
     */
    tool: boolean;
}
interface IWidget {
    helpCenter: boolean;
    appGuide: boolean;
}
/**
 * 移动端胶囊 bar 操作按钮
 */
interface IActionButton {
    /**
     * 编辑按钮
     */
    edit: boolean;
    /**
     * 查找按钮
     */
    search: boolean;
    /**
     * 操作按钮
     */
    operate: boolean;
}
/**
 * 多品类共有业务的个性化配置
 */
interface ICustom$1 {
    /**
     * 私有化 1.6.7 添加
     * 是否开启加载图片缩略图
     * 不默认开启, 只有满足腾讯文档 C 端特定域名条件的, 才会加载缩略图
     */
    imageThumbnail: boolean;
    /**
     * 中台 3.4.0 添加
     * 缩略图配置模板，将其中的占位符替换为缩略宽高，即可生成缩略配置字符串
     * 宽占位符: {width}; 高占位符: {height}
     * 默认使用腾讯云 COS 缩略配置字符串
     * 文档: https://doc.weixin.qq.com/doc/w3_AGEAXAaEACclNs314LjQIuCzEl3rd
     */
    imageThumbnailPath: string;
    /**
     * 中台 3.4.1 添加
     * 不使用缩略图的文件后缀列表
     * 比如部分行业化客户不支持 webp 和 gif 的缩略图，则配置为 ['webp', 'gif'],
     * 没有配置则认为都支持缩略图
     */
    imageThumbnailIgnore: string[];
    /**
     * 私有化 3.4.1 添加
     * 是否打开表格编辑记录，默认不打开，预期只对希音打开(配置为 true)
     * 等 C 端也对接了公共组件，支持编辑记录预览后，再全面打开
     */
    sheetEditRecord: boolean;
    /**
     * 私有化 1.6.7 添加
     * 前端导出是否加密
     */
    exportEncryption: boolean;
    /**
     * 协同编辑房间层连接
     */
    editRoomConnection: boolean;
    /**
     * 插入评论
     */
    insertComment: boolean;
    /**
     * 私有化 1.10 添加
     * AI 文档助手配置
     */
    aiAssistant: Partial<{
        enable: boolean;
        enableTitleBarEntrance: boolean;
        name: string;
        aiLogo: {
            SVGColor: string;
            SVGBlack: string;
            SVGWhite: string;
            gif: string;
            banner: string;
        };
    }>;
    /**
     * 私有化 1.10.1 添加
     * 知识库配置
     */
    wiki: {
        enable: boolean;
    };
    /**
     * vip 功能的图标 & 文案是否展示
     * true 表示展示，false 不展示
     * 文档: https://doc.weixin.qq.com/doc/w3_AGEAXAaEACcCNnIm99tTgRFiuVBvt?scode=AJEAIQdfAAoZGOpiBEAGEAXAaEACc
     */
    vipBadge: boolean;
    /**
     * 上传附件的附件大小限制，单位为 Byte
     * 对接的 dc 组件是 docs-component-attachment, 判断在品类侧
     * 默认按 300 * 1024 * 1024
    */
    attachmentSizeLimit: number;
}
/**
 * 标题栏
 */
interface ITitlebar {
    /**
     * 「PC端」账户头像
     */
    account: boolean;
    /**
     * 「PC端」插件按钮
     */
    addon: boolean;
    /**
     * 「PC端」B端添加成员
     */
    addMember: boolean;
    /**
     * 「PC端」AI 助手
     */
    aiAssistant: boolean;
    /**
     * 「PC端」权限按钮
     */
    auth: boolean;
    /**
     * 「PC端」本地文件自动保存开关
     */
    autoSaveSwitch: boolean;
    /**
     * 「移动端」返回按钮
     */
    back: boolean;
    /**
     * 「PC端」面包屑（文件路径）
     */
    breadcrumb: boolean;
    /**
     * 「PC端」「移动端」收藏按钮
     */
    collect: boolean;
    /**
     * 「PC端」「移动端」当前在线的协作者
     */
    collaborators: boolean;
    /**
     * 「PC端」编辑菜单
     */
    collapseMenu: boolean;
    /**
     * 「移动端」word 和 pdf 用来组装新 logo 的标示字段
     */
    customLogo: boolean;
    /**
     * 「PC端」B端讨论区
     */
    discussion: boolean;
    /**
     * 「PC端」拉端组件
     */
    downloadClient: boolean;
    /**
     * 「PC端」云文档保存按钮
     */
    driveFileSave: boolean;
    /**
     * 「PC端」外部标识
    */
    external: boolean;
    /**
     * 「PC端」文件菜单
     */
    fileMenu: boolean;
    /**
     * 「PC端」布局模式
     */
    layout: 'default' | 'wework';
    /**
     * 「PC端」logo，小房子以及发布态文档 logo
     */
    logo: Partial<ILogo> | false;
    /**
     * 「PC端」本地文件编辑的 tag
     */
    localFileTag: boolean;
    /**
     * 「PC端」本地文件编辑的 logo
     */
    localFileLogo: boolean;
    /**
     * 「PC端」本地文件编辑的保存按钮
     */
    localFileSave: boolean;
    /**
     * 「PC端」本地文件编辑的未登录时候的提示
     */
    localLoginTip: boolean;
    /**
     * 「PC端」本地文件转在线编辑
     */
    localFileTransfer: boolean;
    /**
     * 「PC端」本地文件上传至云端
     */
    localFileUpload: boolean;
    /**
     * 「PC端」「移动端」登录按钮
     */
    login: boolean;
    /**
     * 主菜单
     */
    mainMenu: boolean;
    /**
     * 「PC端」「移动端」成员组件
     */
    member: boolean;
    /**
     * 「PC端」移动到
     */
    move: boolean;
    /**
     * 是否展示订阅通知
    */
    notification: boolean;
    /**
     * 「PC端」演示按钮
     */
    playAction: boolean;
    /**
     * 演示
     */
    presentation: boolean;
    /**
     * 「PC端」打印按钮
     */
    print: boolean;
    /**
     * 「PC端」只能查看标识
     */
    readOnly: boolean;
    /**
     * 「移动端」关联收集表
     */
    relateFormMenu: boolean;
    /**
     * 远程播放
     */
    remotePlay: boolean;
    /**
     *「PC端」重命名按钮
     */
    rename: boolean;
    /**
     * 「PC端」保存状态提示
     */
    saveState: boolean;
    /**
     * 「移动端」保存提示
     */
    saveTip: boolean;
    /**
     * 「PC端」「移动端」分享按钮
     */
    share: boolean;
    /**
     * 「PC端」私有化分享组件
     */
    sharePrivate: boolean;
    /**
     * 「PC端」「移动端」标题
     */
    title: boolean;
    /**
     * 「PC端」广告
     */
    titlebarAd: boolean;
    /**
     * 标题栏
     */
    titlebar: boolean;
    /**
     * @deprecated 原小房子右边的竖线，已废弃
     */
    verticalLine: boolean;
    /**
     * 私有化密级标签
     */
    confidentialityTag: boolean;
}
/**
 * 顶部 Home 键
 */
interface ILogo {
    /**
     * 可点击
     */
    clickable: boolean;
    /**
     * 图标的 url 或图标的 css class
     */
    logoUrl: string;
    /**
     * 展示红点
     */
    badge: boolean;
}
type IDocEntriesConfig = Record<string, Partial<{
    /**
     * 是否禁用该品类所有入口.
     */
    disabled?: boolean;
    /**
     * 是否禁用该品类新建、模板的入口.
     */
    disableCreate?: boolean;
}>>;
/**
 * @deprecated 后续和 ICommonConfig tenantConfig 一起移除
 * @ yider
 * 租户信息
 */
interface ITenantConfig {
    /**
     * 租户名
     */
    tenantName: string;
    /**
     * 租户文档名
     */
    tenantDocName: string;
    /**
     * 租户相关 logo 信息
     */
    tenantLogo: {
        /**
         * 黑色 logo svg 地址
         */
        SVGBlack: string;
        /**
         * 黑色 Solid 风格 logo svg 地址
         */
        SVGSolidBlack: string;
        /**
         * 彩色 logo svg 地址
         */
        SVGColor: string;
    };
}

type IWebOfficeConfig = Partial<ICommonConfig> & Partial<IExtraCommonConfig> & Partial<IWebOfficeCustomConfig> & Partial<ICustom> & Record<string, boolean | Record<string, unknown>>;
interface ICustom {
    custom: Partial<{
        editRoomConnection: boolean;
    } & Record<string, unknown>>;
}
interface IExtraCommonConfig {
    statusbar: boolean | Record<string, unknown>;
}
interface IWebOfficeCustomConfig {
    weboffice: {
        [key: string]: unknown;
        headers?: Record<string, string>;
    };
}

declare enum LogLevel {
    LOG = "log",
    INFO = "info",
    WARN = "warn",
    ERROR = "error"
}
type ConsoleMethod = (...args: unknown[]) => void;
interface ConsoleLike {
    log: ConsoleMethod;
    info: ConsoleMethod;
    warn: ConsoleMethod;
    error: ConsoleMethod;
}

declare class Iframe implements IDisposable {
    iframe: HTMLIFrameElement;
    iframeName: string;
    constructor(config: IIframeConfig);
    appendTo(mount?: string | HTMLElement): void;
    dispose(): void;
}
interface ITips {
    showTip(content: string, parentDom: HTMLElement): void;
    hideTip(): void;
}
declare class Tips {
    private container;
    private tipDom;
    constructor(container: HTMLElement, loadingUI?: ITips);
    showTip(content: string): void;
    hideTip(): void;
}

/**
 * @author luissszhang
 * 要加参数提前沟通一下！
 */

interface IOpenTencentDoc {
    tencentdocUrl: string;
}
interface IOpenOfficeId {
    fileId: string;
    /**
     * 签名字段，校验签名内容，必填
     */
    signature: signData;
    /**
     * 文件类型
     */
    officeType?: OfficeType;
    /**
     * 控制文档权限，可根据不同用户传入不同token控制用户权限
     */
    fileToken?: string;
    /**
     * 文档历史版本
     */
    version?: string;
}
interface IOpenOfficeFile {
    file: File;
    /**
     * 签名字段，校验签名内容，必填
     */
    signature: signData;
    /**
     * 文件类型
     */
    officeType?: OfficeType;
    /**
     * 控制文档权限，可根据不同用户传入不同token控制用户权限
     */
    fileToken?: string;
}
declare const IConfig: unique symbol;
type IConfig = IWebOfficeBasicConfig & // 应用基础相关
IIframeConfig & // iframe设置相关
ICacConfig & // 文档配置相关
IDocConfig & // 文档设置相关
IPreheatFeatureConfig & // 容器预热相关
ILoadingConfig & // loading相关配置
IExtraConfig;
interface IWebOfficeBasicConfig {
    /**
     * 应用AppId，通过申请获得，请联系相关人员，必填
     */
    appId: string;
    /**
     * 控制文档权限，可根据不同用户传入不同token控制用户权限
     */
    fileToken?: string;
}
interface IIframeConfig {
    /**
     * 私有化环境部署地址
     */
    endpoint?: string;
    /**
     * 将iframe挂载的元素，可以是id，也可以是dom
     */
    mount?: string | HTMLElement;
    /**
     * iframe 的 allow 属性，已经有默认值 defaultAllowList，这里是用户额外自定义的
     */
    allowList?: string[];
    /**
     * iframe src 自定义的参数，优先级低于 SDK 的默认参数
     */
    srcParams?: URLSearchParams | Record<string, string> | string;
    /**
     * iframe src 格式化函数, 可在打开前最终处理src
     */
    srcFormatter?: (src: string) => string;
}
interface ICacConfig {
    /**
     * 文档配置相关
     * 即品类cac配置
     */
    docsConfig?: IWebOfficeConfig;
}
interface IDocConfig {
    /**
     * 文档语言，默认为浏览器语言，有效值:
     * zh-CN、zh-HK、en-US
     */
    language?: string;
    /**
     * 配置文档打开的工具栏是【简约工具栏】还是【专业工具栏】。也可以在 `srcParams` 参数中传入同名参数实现。
     */
    toolbarMode?: 'single' | 'double';
}
interface IPreheatFeatureConfig {
    /**
     * 文档类型，开启预热功能时必传
     */
    officeType?: OfficeType;
    /**
     * 开启预热容器的开关
     */
    enablePreheatContainer?: boolean;
}
interface IExtraConfig {
    /**
     * 文档历史版本
     */
    version?: string;
    /**
     * 关闭logCollection监听操作
     */
    disableLogCollection?: boolean;
    /**
     * 是否 kv 缓存 token，默认为 `false`。如果 `true` 的话则通过 URL 传递 token 参数保证可以多次打开文档
     */
    disableCacheToken?: boolean;
    /**
     * 是否 在请求中携带header，如果跨域头有问题时可以临时解决
     */
    disableRequestHeader?: boolean;
    /**
     * 开启请求接口的缓存策略，可以优化100-300ms的二次打开耗时，特别是低端机或网络不好的情况下优化更多
     */
    enableRequestCache?: boolean;
    /**
     * 第三方携带的透传参数
     */
    customHeaders?: Record<string, string>;
    /**
     * 第三方携带的透传参数
     */
    customParams?: Record<string, string>;
    /**
     * 传入logger
     */
    logger?: ConsoleLike;
    /**
     * api超时时间
     */
    timeout?: number;
}
interface ILoadingConfig {
    hideLoading?: boolean;
    loadingUI?: ITips;
}
declare enum OfficeType {
    DOC = "doc",
    DOCX = "docx",
    TXT = "txt",
    CSV = "csv",
    XLS = "xls",
    XLSX = "xlsx",
    PPT = "ppt",
    PPTX = "pptx",
    PDF = "pdf",
    OFD = "ofd"
}
interface TokenData {
    token: string;
    timeout: number;
}
interface signData {
    sign: string;
    nonce: string;
    timeStamp: number;
}

interface IConfigService {
    config: IConfig;
    setHeaders(key: string, value: string): void;
    /**
     * 是否是打开腾讯文档逻辑
     *
     * 为true代表传入的配置项里有tencentdocUrl
     */
    isOpenTencentdoc(config: IConfig): config is IConfig & IOpenTencentDoc;
    /**
     * 是否是打开第三方office文档逻辑
     *
     * 为true代表传入的配置项里有fileId
     */
    isOpenOfficeFile(config: IConfig): config is IConfig & IOpenOfficeFile;
    /**
     * 是否是打开第三方office文档逻辑
     *
     * 为true代表传入的配置项里有fileId
     */
    isOpenOfficeId(config: IConfig): config is IConfig & IOpenOfficeId;
}
declare class ConfigService implements IConfigService {
    private _config;
    constructor(_config: IConfig);
    get config(): IConfig;
    set config(val: IConfig);
    setHeaders(key: string, value: string): void;
    isOpenOfficeId(config: IConfig): config is IConfig & IOpenOfficeId;
    isOpenOfficeFile(config: IConfig): config is IConfig & IOpenOfficeFile;
    isOpenTencentdoc(config: IConfig): config is IConfig & IOpenTencentDoc;
}

/**
 * 用来外挂api的feature
 */
declare class ApiFeature {
    protected container: IContainer;
    constructor(container: IContainer);
    reload(): void;
    save(): Promise<WebOfficeApiResponse<unknown>>;
    getDocType(): string;
    reportLogToWeblog(): void;
    generateCompressLog(): void;
    copyLogToClipboard(): void;
    updateConfig(config: Partial<IConfig | IOpenOfficeId | IOpenTencentDoc | IOpenOfficeFile>): void;
    refreshToken(fileToken: string): Promise<boolean>;
}

declare class UIService {
    private config;
    container: HTMLDivElement;
    iframeInstance: Iframe;
    tipsInstance: Tips;
    constructor(config: IConfig);
    hideUI(): void;
    showUI(): void;
    dispose(): void;
}

/**
 * sdk实例
 */
interface IWebOfficeInstance extends ApiFeature {
    /**
     * 品类API，必须在ready之后调用才有值
     */
    Application: any;
    ApiEvent: IEventEmitter;
    ready(): Promise<void>;
    /**
     * instance.destroy()之后
     * 记得执行instance = null;
     */
    destroy(): void;
}
declare const WebOfficeInstance_base: typeof ApiFeature & Constructor<IDisposableRegister>;
/**
 * instance.ApiEvent
 *
 * instance.Application
 *
 * instance.ready()
 *
 * instance.destroy()
 *
 * instance.save()
 *
 * instance.getDocType()
 */
declare class WebOfficeInstance extends WebOfficeInstance_base implements IWebOfficeInstance {
    ApiEvent: IEventEmitter;
    private getApplicationCommand;
    configService: ConfigService;
    uiService: UIService;
    isDispose: boolean;
    /**
     * TODO: 现在是any，之后有时间了再看看有没有必要吧类型引用上
     * 不建议搞类型的原因：类型要频繁更新，接入方也要频繁更新sdk
     */
    Application: unknown;
    constructor(ApiEvent: IEventEmitter, getApplicationCommand: GetApplicationCommand, container: IContainer, configService: ConfigService, uiService: UIService);
    ready(): Promise<void>;
    save(): Promise<WebOfficeApiResponse<unknown>>;
    destroy(): void;
    private init;
    private initLogCollection;
    private listenSoftKeyboard;
}

/**
 * 日志收集类，将日志收集到统一的位置，然后统一处理：比如复制到剪切板，或者上报至weblog
 */
declare class LogCollection {
    private readonly isInWebOfficeIframe;
    private weblogInited;
    private logDataArray;
    constructor(isInWebOfficeIframe: boolean);
    write(data: unknown[], level: LogLevel): void;
    /**
     * 初始化weblog
     */
    init(uid?: string): Promise<void>;
    /**
     * 将日志上报到weblog
     */
    reportToWeblog(): Promise<void>;
    /**
     * @returns 生成压缩后的日志
     */
    generateCompressLog(): string;
    /**
     * 将日志压缩并复制到剪切板
     */
    copyToClipboard(): Promise<void>;
    /**
     * 将剪切板的日志内容，解压缩出来
     */
    depressLogText(data: string): any;
    private listenIframeLog;
    private emitIframeLog;
    private initSDK;
}
declare const logCollection: LogCollection;

declare class WebOfficeSdk {
    static OfficeType: typeof OfficeType;
    static version: string;
    version: string;
    instance: WebOfficeInstance;
    private container;
    constructor(config: IConfig);
    openFileId(config: IOpenOfficeId & Partial<IConfig>): IWebOfficeInstance;
    openFile(config: IOpenOfficeId & Partial<IConfig>): IWebOfficeInstance;
    openTencentDocUrl(config: IOpenOfficeId & Partial<IConfig>): IWebOfficeInstance;
    private initInstance;
    /**
     * 根据配置初始化依赖注入容器
     * @param config
     * @returns
     */
    private initService;
    private operateFeature;
    private setConfig;
    private validParams;
    private checkDispose;
}

export { IConfig, type IIframeConfig, type ILoadingConfig, type IOpenOfficeFile, type IOpenOfficeId, type IOpenTencentDoc, type IWebOfficeConfig, type IWebOfficeInstance, OfficeType, type TokenData, WebOfficeSdk, logCollection, type signData };
