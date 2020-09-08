package com.yanbenjun.file.parse.core;

import com.yanbenjun.file.parse.api.Reader;
import com.yanbenjun.file.parse.core.post.infs.PostRowHandler;

public abstract class AbstractReader implements Reader
{
    protected PostRowHandler startPostRowHandler;
}
