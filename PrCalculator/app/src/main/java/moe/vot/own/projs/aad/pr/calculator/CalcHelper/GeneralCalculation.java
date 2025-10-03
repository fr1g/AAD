package moe.vot.own.projs.aad.pr.calculator.CalcHelper;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

public class GeneralCalculation implements AutoCloseable{
    public Context rhino;
    public Scriptable scope;

    public String calc(String jsCode) {

        Object result = rhino.evaluateString(scope, jsCode, "<calcEase>", 1, null);
        return Context.toString(result);

    }

    public GeneralCalculation(){
        rhino = Context.enter();
        rhino.setOptimizationLevel(-1);
        scope = rhino.initStandardObjects();
    }

    @Override
    public void close() {
        Context.exit();
    }
}
