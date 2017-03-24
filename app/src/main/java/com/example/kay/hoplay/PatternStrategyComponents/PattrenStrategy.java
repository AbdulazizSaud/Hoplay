package com.example.kay.hoplay.PatternStrategyComponents;

import java.util.HashMap;

/**
 * Created by Kay on 2/7/2017.
 */

public interface PattrenStrategy<T>
{

    void afterComplete(T results);
}
