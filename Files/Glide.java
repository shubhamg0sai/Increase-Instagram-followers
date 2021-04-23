package com.bumptech.glide;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.ParcelFileDescriptor;
import android.support.p000v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Engine;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.engine.cache.MemoryCache;
import com.bumptech.glide.load.engine.prefill.BitmapPreFiller;
import com.bumptech.glide.load.engine.prefill.PreFillType;
import com.bumptech.glide.load.model.GenericLoaderFactory;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.ImageVideoWrapper;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.file_descriptor.FileDescriptorFileLoader;
import com.bumptech.glide.load.model.file_descriptor.FileDescriptorResourceLoader;
import com.bumptech.glide.load.model.file_descriptor.FileDescriptorStringLoader;
import com.bumptech.glide.load.model.file_descriptor.FileDescriptorUriLoader;
import com.bumptech.glide.load.model.stream.HttpUrlGlideUrlLoader;
import com.bumptech.glide.load.model.stream.StreamByteArrayLoader;
import com.bumptech.glide.load.model.stream.StreamFileLoader;
import com.bumptech.glide.load.model.stream.StreamResourceLoader;
import com.bumptech.glide.load.model.stream.StreamStringLoader;
import com.bumptech.glide.load.model.stream.StreamUriLoader;
import com.bumptech.glide.load.model.stream.StreamUrlLoader;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.FileDescriptorBitmapDataLoadProvider;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.load.resource.bitmap.ImageVideoDataLoadProvider;
import com.bumptech.glide.load.resource.bitmap.StreamBitmapDataLoadProvider;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.load.resource.file.StreamFileDataLoadProvider;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.load.resource.gif.GifDrawableLoadProvider;
import com.bumptech.glide.load.resource.gifbitmap.GifBitmapWrapper;
import com.bumptech.glide.load.resource.gifbitmap.GifBitmapWrapperTransformation;
import com.bumptech.glide.load.resource.gifbitmap.ImageVideoGifDrawableLoadProvider;
import com.bumptech.glide.load.resource.transcode.GifBitmapWrapperDrawableTranscoder;
import com.bumptech.glide.load.resource.transcode.GlideBitmapDrawableTranscoder;
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder;
import com.bumptech.glide.load.resource.transcode.TranscoderRegistry;
import com.bumptech.glide.manager.RequestManagerRetriever;
import com.bumptech.glide.module.GlideModule;
import com.bumptech.glide.module.ManifestParser;
import com.bumptech.glide.provider.DataLoadProvider;
import com.bumptech.glide.provider.DataLoadProviderRegistry;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ImageViewTargetFactory;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.target.ViewTarget;
import com.bumptech.glide.util.Util;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.List;

public class Glide {
    private static final String TAG = "Glide";
    private static volatile Glide glide;
    private static boolean modulesEnabled = true;
    private final CenterCrop bitmapCenterCrop;
    private final FitCenter bitmapFitCenter;
    private final BitmapPool bitmapPool;
    private final BitmapPreFiller bitmapPreFiller;
    private final DataLoadProviderRegistry dataLoadProviderRegistry;
    private final DecodeFormat decodeFormat;
    private final GifBitmapWrapperTransformation drawableCenterCrop;
    private final GifBitmapWrapperTransformation drawableFitCenter;
    private final Engine engine;
    private final ImageViewTargetFactory imageViewTargetFactory;
    private final GenericLoaderFactory loaderFactory;
    private final Handler mainHandler;
    private final MemoryCache memoryCache;
    private final TranscoderRegistry transcoderRegistry;

    public static File getPhotoCacheDir(Context context) {
        return getPhotoCacheDir(context, DiskCache.Factory.DEFAULT_DISK_CACHE_DIR);
    }

    public static File getPhotoCacheDir(Context context, String str) {
        File file;
        String cacheName = str;
        File cacheDir = context.getCacheDir();
        if (cacheDir != null) {
            new File(cacheDir, cacheName);
            File result = file;
            if (result.mkdirs() || (result.exists() && result.isDirectory())) {
                return result;
            }
            return null;
        }
        if (Log.isLoggable(TAG, 6)) {
            int e = Log.e(TAG, "default disk cache dir is null");
        }
        return null;
    }

    public static void setModulesEnabled(boolean z) {
        Throwable th;
        boolean enabled = z;
        Class<Glide> cls = Glide.class;
        Class<Glide> cls2 = cls;
        synchronized (cls) {
            try {
                if (glide != null) {
                    Throwable th2 = th;
                    new IllegalArgumentException("Glide singleton already exists.");
                    throw th2;
                }
                modulesEnabled = enabled;
            } catch (Throwable th3) {
                Throwable th4 = th3;
                Class<Glide> cls3 = cls2;
                throw th4;
            }
        }
    }

    /* JADX INFO: finally extract failed */
    public static Glide get(Context context) {
        GlideBuilder glideBuilder;
        Context context2 = context;
        if (glide == null) {
            Class<Glide> cls = Glide.class;
            Class<Glide> cls2 = cls;
            synchronized (cls) {
                try {
                    if (glide == null) {
                        Context applicationContext = context2.getApplicationContext();
                        new GlideBuilder(applicationContext);
                        GlideBuilder builder = glideBuilder;
                        List<GlideModule> modules = parseGlideModules(applicationContext);
                        for (GlideModule module : modules) {
                            module.applyOptions(applicationContext, builder);
                        }
                        glide = builder.createGlide();
                        for (GlideModule module2 : modules) {
                            module2.registerComponents(applicationContext, glide);
                        }
                    }
                } catch (Throwable th) {
                    while (true) {
                        Throwable th2 = th;
                        Class<Glide> cls3 = cls2;
                        throw th2;
                    }
                }
            }
        }
        return glide;
    }

    private static List<GlideModule> parseGlideModules(Context context) {
        ManifestParser manifestParser;
        Context applicationContext = context;
        if (!modulesEnabled) {
            return Collections.emptyList();
        }
        new ManifestParser(applicationContext);
        return manifestParser.parse();
    }

    @Deprecated
    public static boolean isSetup() {
        return glide != null;
    }

    @Deprecated
    public static void setup(GlideBuilder glideBuilder) {
        Throwable th;
        GlideBuilder builder = glideBuilder;
        if (isSetup()) {
            Throwable th2 = th;
            new IllegalArgumentException("Glide is already setup, check with isSetup() first");
            throw th2;
        }
        glide = builder.createGlide();
    }

    static void tearDown() {
        glide = null;
        modulesEnabled = true;
    }

    Glide(Engine engine2, MemoryCache memoryCache2, BitmapPool bitmapPool2, Context context, DecodeFormat decodeFormat2) {
        ImageViewTargetFactory imageViewTargetFactory2;
        TranscoderRegistry transcoderRegistry2;
        GenericLoaderFactory genericLoaderFactory;
        Handler handler;
        BitmapPreFiller bitmapPreFiller2;
        DataLoadProviderRegistry dataLoadProviderRegistry2;
        StreamBitmapDataLoadProvider streamBitmapDataLoadProvider;
        FileDescriptorBitmapDataLoadProvider fileDescriptorBitmapDataLoadProvider;
        ImageVideoDataLoadProvider imageVideoDataLoadProvider;
        GifDrawableLoadProvider gifDrawableLoadProvider;
        DataLoadProvider dataLoadProvider;
        DataLoadProvider dataLoadProvider2;
        ModelLoaderFactory modelLoaderFactory;
        ModelLoaderFactory modelLoaderFactory2;
        ModelLoaderFactory modelLoaderFactory3;
        ModelLoaderFactory modelLoaderFactory4;
        ModelLoaderFactory modelLoaderFactory5;
        ModelLoaderFactory modelLoaderFactory6;
        ModelLoaderFactory modelLoaderFactory7;
        ModelLoaderFactory modelLoaderFactory8;
        ModelLoaderFactory modelLoaderFactory9;
        ModelLoaderFactory modelLoaderFactory10;
        ModelLoaderFactory modelLoaderFactory11;
        ModelLoaderFactory modelLoaderFactory12;
        ModelLoaderFactory modelLoaderFactory13;
        ResourceTranscoder resourceTranscoder;
        ResourceTranscoder resourceTranscoder2;
        ResourceTranscoder resourceTranscoder3;
        CenterCrop centerCrop;
        GifBitmapWrapperTransformation gifBitmapWrapperTransformation;
        FitCenter fitCenter;
        GifBitmapWrapperTransformation gifBitmapWrapperTransformation2;
        MemoryCache memoryCache3 = memoryCache2;
        BitmapPool bitmapPool3 = bitmapPool2;
        Context context2 = context;
        DecodeFormat decodeFormat3 = decodeFormat2;
        new ImageViewTargetFactory();
        this.imageViewTargetFactory = imageViewTargetFactory2;
        new TranscoderRegistry();
        this.transcoderRegistry = transcoderRegistry2;
        this.engine = engine2;
        this.bitmapPool = bitmapPool3;
        this.memoryCache = memoryCache3;
        this.decodeFormat = decodeFormat3;
        new GenericLoaderFactory(context2);
        this.loaderFactory = genericLoaderFactory;
        new Handler(Looper.getMainLooper());
        this.mainHandler = handler;
        new BitmapPreFiller(memoryCache3, bitmapPool3, decodeFormat3);
        this.bitmapPreFiller = bitmapPreFiller2;
        new DataLoadProviderRegistry();
        this.dataLoadProviderRegistry = dataLoadProviderRegistry2;
        new StreamBitmapDataLoadProvider(bitmapPool3, decodeFormat3);
        StreamBitmapDataLoadProvider streamBitmapLoadProvider = streamBitmapDataLoadProvider;
        this.dataLoadProviderRegistry.register(InputStream.class, Bitmap.class, streamBitmapLoadProvider);
        new FileDescriptorBitmapDataLoadProvider(bitmapPool3, decodeFormat3);
        FileDescriptorBitmapDataLoadProvider fileDescriptorLoadProvider = fileDescriptorBitmapDataLoadProvider;
        this.dataLoadProviderRegistry.register(ParcelFileDescriptor.class, Bitmap.class, fileDescriptorLoadProvider);
        new ImageVideoDataLoadProvider(streamBitmapLoadProvider, fileDescriptorLoadProvider);
        ImageVideoDataLoadProvider imageVideoDataLoadProvider2 = imageVideoDataLoadProvider;
        this.dataLoadProviderRegistry.register(ImageVideoWrapper.class, Bitmap.class, imageVideoDataLoadProvider2);
        new GifDrawableLoadProvider(context2, bitmapPool3);
        GifDrawableLoadProvider gifDrawableLoadProvider2 = gifDrawableLoadProvider;
        this.dataLoadProviderRegistry.register(InputStream.class, GifDrawable.class, gifDrawableLoadProvider2);
        new ImageVideoGifDrawableLoadProvider(imageVideoDataLoadProvider2, gifDrawableLoadProvider2, bitmapPool3);
        this.dataLoadProviderRegistry.register(ImageVideoWrapper.class, GifBitmapWrapper.class, dataLoadProvider);
        new StreamFileDataLoadProvider();
        this.dataLoadProviderRegistry.register(InputStream.class, File.class, dataLoadProvider2);
        new FileDescriptorFileLoader.Factory();
        register(File.class, ParcelFileDescriptor.class, modelLoaderFactory);
        new StreamFileLoader.Factory();
        register(File.class, InputStream.class, modelLoaderFactory2);
        new FileDescriptorResourceLoader.Factory();
        register(Integer.TYPE, ParcelFileDescriptor.class, modelLoaderFactory3);
        new StreamResourceLoader.Factory();
        register(Integer.TYPE, InputStream.class, modelLoaderFactory4);
        new FileDescriptorResourceLoader.Factory();
        register(Integer.class, ParcelFileDescriptor.class, modelLoaderFactory5);
        new StreamResourceLoader.Factory();
        register(Integer.class, InputStream.class, modelLoaderFactory6);
        new FileDescriptorStringLoader.Factory();
        register(String.class, ParcelFileDescriptor.class, modelLoaderFactory7);
        new StreamStringLoader.Factory();
        register(String.class, InputStream.class, modelLoaderFactory8);
        new FileDescriptorUriLoader.Factory();
        register(Uri.class, ParcelFileDescriptor.class, modelLoaderFactory9);
        new StreamUriLoader.Factory();
        register(Uri.class, InputStream.class, modelLoaderFactory10);
        new StreamUrlLoader.Factory();
        register(URL.class, InputStream.class, modelLoaderFactory11);
        new HttpUrlGlideUrlLoader.Factory();
        register(GlideUrl.class, InputStream.class, modelLoaderFactory12);
        new StreamByteArrayLoader.Factory();
        register(byte[].class, InputStream.class, modelLoaderFactory13);
        new GlideBitmapDrawableTranscoder(context2.getResources(), bitmapPool3);
        this.transcoderRegistry.register(Bitmap.class, GlideBitmapDrawable.class, resourceTranscoder);
        new GlideBitmapDrawableTranscoder(context2.getResources(), bitmapPool3);
        new GifBitmapWrapperDrawableTranscoder(resourceTranscoder3);
        this.transcoderRegistry.register(GifBitmapWrapper.class, GlideDrawable.class, resourceTranscoder2);
        new CenterCrop(bitmapPool3);
        this.bitmapCenterCrop = centerCrop;
        new GifBitmapWrapperTransformation(bitmapPool3, (Transformation<Bitmap>) this.bitmapCenterCrop);
        this.drawableCenterCrop = gifBitmapWrapperTransformation;
        new FitCenter(bitmapPool3);
        this.bitmapFitCenter = fitCenter;
        new GifBitmapWrapperTransformation(bitmapPool3, (Transformation<Bitmap>) this.bitmapFitCenter);
        this.drawableFitCenter = gifBitmapWrapperTransformation2;
    }

    public BitmapPool getBitmapPool() {
        return this.bitmapPool;
    }

    /* access modifiers changed from: package-private */
    public <Z, R> ResourceTranscoder<Z, R> buildTranscoder(Class<Z> decodedClass, Class<R> transcodedClass) {
        return this.transcoderRegistry.get(decodedClass, transcodedClass);
    }

    /* access modifiers changed from: package-private */
    public <T, Z> DataLoadProvider<T, Z> buildDataProvider(Class<T> dataClass, Class<Z> decodedClass) {
        return this.dataLoadProviderRegistry.get(dataClass, decodedClass);
    }

    /* access modifiers changed from: package-private */
    public <R> Target<R> buildImageViewTarget(ImageView imageView, Class<R> transcodedClass) {
        return this.imageViewTargetFactory.buildTarget(imageView, transcodedClass);
    }

    /* access modifiers changed from: package-private */
    public Engine getEngine() {
        return this.engine;
    }

    /* access modifiers changed from: package-private */
    public CenterCrop getBitmapCenterCrop() {
        return this.bitmapCenterCrop;
    }

    /* access modifiers changed from: package-private */
    public FitCenter getBitmapFitCenter() {
        return this.bitmapFitCenter;
    }

    /* access modifiers changed from: package-private */
    public GifBitmapWrapperTransformation getDrawableCenterCrop() {
        return this.drawableCenterCrop;
    }

    /* access modifiers changed from: package-private */
    public GifBitmapWrapperTransformation getDrawableFitCenter() {
        return this.drawableFitCenter;
    }

    /* access modifiers changed from: package-private */
    public Handler getMainHandler() {
        return this.mainHandler;
    }

    /* access modifiers changed from: package-private */
    public DecodeFormat getDecodeFormat() {
        return this.decodeFormat;
    }

    private GenericLoaderFactory getLoaderFactory() {
        return this.loaderFactory;
    }

    public void preFillBitmapPool(PreFillType.Builder... bitmapAttributeBuilders) {
        this.bitmapPreFiller.preFill(bitmapAttributeBuilders);
    }

    public void clearMemory() {
        Util.assertMainThread();
        this.memoryCache.clearMemory();
        this.bitmapPool.clearMemory();
    }

    public void trimMemory(int i) {
        int level = i;
        Util.assertMainThread();
        this.memoryCache.trimMemory(level);
        this.bitmapPool.trimMemory(level);
    }

    public void clearDiskCache() {
        Util.assertBackgroundThread();
        getEngine().clearDiskCache();
    }

    public void setMemoryCategory(MemoryCategory memoryCategory) {
        MemoryCategory memoryCategory2 = memoryCategory;
        Util.assertMainThread();
        this.memoryCache.setSizeMultiplier(memoryCategory2.getMultiplier());
        this.bitmapPool.setSizeMultiplier(memoryCategory2.getMultiplier());
    }

    public static void clear(Target<?> target) {
        Target<?> target2 = target;
        Util.assertMainThread();
        Request request = target2.getRequest();
        if (request != null) {
            request.clear();
            target2.setRequest((Request) null);
        }
    }

    public static void clear(FutureTarget<?> target) {
        target.clear();
    }

    public static void clear(View view) {
        Target target;
        new ClearTarget(view);
        clear((Target<?>) target);
    }

    public <T, Y> void register(Class<T> modelClass, Class<Y> resourceClass, ModelLoaderFactory<T, Y> factory) {
        ModelLoaderFactory<T, Y> removed = this.loaderFactory.register(modelClass, resourceClass, factory);
        if (removed != null) {
            removed.teardown();
        }
    }

    @Deprecated
    public <T, Y> void unregister(Class<T> modelClass, Class<Y> resourceClass) {
        ModelLoaderFactory<T, Y> removed = this.loaderFactory.unregister(modelClass, resourceClass);
        if (removed != null) {
            removed.teardown();
        }
    }

    public static <T, Y> ModelLoader<T, Y> buildModelLoader(Class<T> cls, Class<Y> cls2, Context context) {
        Class<T> modelClass = cls;
        Class<Y> resourceClass = cls2;
        Context context2 = context;
        if (modelClass != null) {
            return get(context2).getLoaderFactory().buildModelLoader(modelClass, resourceClass);
        }
        if (Log.isLoggable(TAG, 3)) {
            int d = Log.d(TAG, "Unable to load null model, setting placeholder only");
        }
        return null;
    }

    public static <T, Y> ModelLoader<T, Y> buildModelLoader(T t, Class<Y> resourceClass, Context context) {
        T model = t;
        return buildModelLoader(model != null ? model.getClass() : null, resourceClass, context);
    }

    public static <T> ModelLoader<T, InputStream> buildStreamModelLoader(Class<T> modelClass, Context context) {
        return buildModelLoader(modelClass, InputStream.class, context);
    }

    public static <T> ModelLoader<T, InputStream> buildStreamModelLoader(T model, Context context) {
        return buildModelLoader(model, InputStream.class, context);
    }

    public static <T> ModelLoader<T, ParcelFileDescriptor> buildFileDescriptorModelLoader(Class<T> modelClass, Context context) {
        return buildModelLoader(modelClass, ParcelFileDescriptor.class, context);
    }

    public static <T> ModelLoader<T, ParcelFileDescriptor> buildFileDescriptorModelLoader(T model, Context context) {
        return buildModelLoader(model, ParcelFileDescriptor.class, context);
    }

    public static RequestManager with(Context context) {
        return RequestManagerRetriever.get().get(context);
    }

    public static RequestManager with(Activity activity) {
        return RequestManagerRetriever.get().get(activity);
    }

    public static RequestManager with(FragmentActivity activity) {
        return RequestManagerRetriever.get().get(activity);
    }

    @TargetApi(11)
    public static RequestManager with(Fragment fragment) {
        return RequestManagerRetriever.get().get(fragment);
    }

    public static RequestManager with(android.support.p000v4.app.Fragment fragment) {
        return RequestManagerRetriever.get().get(fragment);
    }

    private static class ClearTarget extends ViewTarget<View, Object> {
        /* JADX INFO: super call moved to the top of the method (can break code semantics) */
        public ClearTarget(View view) {
            super(view);
        }

        public void onLoadStarted(Drawable placeholder) {
        }

        public void onLoadFailed(Exception e, Drawable errorDrawable) {
        }

        public void onResourceReady(Object resource, GlideAnimation<? super Object> glideAnimation) {
        }

        public void onLoadCleared(Drawable placeholder) {
        }
    }
}
