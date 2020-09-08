package com.yanbenjun.file.parse.core;

import com.yanbenjun.file.config.element.BaseParseFileInfo;
import com.yanbenjun.file.parse.core.post.infs.PostRowHandler;
import com.yanbenjun.file.parse.core.post.may.CacheHandler;
import com.yanbenjun.file.parse.core.post.may.ExcludeFiltHandler;
import com.yanbenjun.file.parse.core.post.may.MapWrapHandler;
import com.yanbenjun.file.parse.core.post.may.NormalPrinter;
import com.yanbenjun.file.parse.core.post.must.SameTitleMergeHandler;
import com.yanbenjun.file.parse.core.post.must.TypeConvertHandler;
import com.yanbenjun.file.parse.extract.FileParseExtractor;

public class PostHandleChain
{
    private FileParseExtractor startHandler;

    public PostHandleChain(FileParseExtractor startHandler)
    {
        this.startHandler = startHandler;
    }
    
    public static PostHandleChain getDefaultChain()
    {
        NormalPrinter np = new NormalPrinter();
        MapWrapHandler mwh = new MapWrapHandler();
        ExcludeFiltHandler efh = new ExcludeFiltHandler();
        SameTitleMergeHandler stmh = new SameTitleMergeHandler();
        stmh.next(efh).next(mwh).next(np);
        TypeConvertHandler tch = new TypeConvertHandler(stmh);

        FileParseExtractor extrator = new FileParseExtractor(tch);
        return new PostHandleChain(extrator);
    }
    
    public static PostHandleChain getBuildinChain()
    {
        
        MapWrapHandler mwh = new MapWrapHandler();
        ExcludeFiltHandler efh = new ExcludeFiltHandler();
        SameTitleMergeHandler stmh = new SameTitleMergeHandler();
        stmh.next(efh).next(mwh);
        TypeConvertHandler tch = new TypeConvertHandler(stmh);
        FileParseExtractor extrator = new FileParseExtractor(tch);
        return new PostHandleChain(extrator);
    }

    public static PostHandleChain getXmlDefaultChain()
    {
        CacheHandler cache = new CacheHandler();
        FileParseExtractor extrator = new FileParseExtractor(cache);
        return new PostHandleChain(extrator);
    }
    
    public void tail(PostRowHandler postRowHandler) {
        PostRowHandler tail = this.startHandler;
        PostRowHandler pre = tail;
        while((tail = this.startHandler.next()) != null) {
            pre = tail;
        }
        pre.next(postRowHandler);
    }

    public void run(BaseParseFileInfo baseFileInfo)
    {
        this.startHandler.handle(baseFileInfo);
    }

    public CacheHandler getCacheHandler()
    {
        PostRowHandler prHandler = null;
        while ((prHandler = this.startHandler.next()) != null)
        {
            if (prHandler instanceof CacheHandler)
            {
                return (CacheHandler) prHandler;
            }
        }
        return null;
    }
}
