package com.gamix;

import org.junit.runner.RunWith;
import com.googlecode.junittoolbox.SuiteClasses;
import com.googlecode.junittoolbox.WildcardPatternSuite;

@RunWith(WildcardPatternSuite.class)
@SuiteClasses({"**/*Test.class"})
public class RunAllUnitTests {
}
