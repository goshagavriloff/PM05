package gui.components.director;

import core.forum.Service;
import utils.listener.IListener;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.function.Supplier;

public class ServiceDirector implements Director<Service>{
    private Supplier<Service> _supplier;
    private String _methodName;
    private HashMap<String,String> params;
    private IListener _next;

    @Override
    public Director<Service> build(Supplier<Service> supplier) {
        _supplier=supplier;
        return this;
    }

    @Override
    public Service make() {
        Service service=_supplier.get();
        service.setNext(_next);
        service.methods.get(_methodName).execute(params);
        return service;
    }

    public ServiceDirector method(String _method) {
        this._methodName = _method;
        return this;
    }

    public ServiceDirector params(HashMap<String, String> params) {
        this.params = params;
        return this;
    }

    public ServiceDirector next(IListener next){
        this._next=next;
        return this;
    }



}
