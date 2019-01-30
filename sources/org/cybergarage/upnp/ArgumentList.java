package org.cybergarage.upnp;

import java.util.Vector;

public class ArgumentList extends Vector {
    public static final String ELEM_NAME = "argumentList";

    public C0779b getArgument(int i) {
        return (C0779b) get(i);
    }

    public C0779b getArgument(String str) {
        int size = size();
        for (int i = 0; i < size; i++) {
            C0779b argument = getArgument(i);
            String b = argument.m190b();
            if (b != null && b.equals(str)) {
                return argument;
            }
        }
        return null;
    }

    public void set(ArgumentList argumentList) {
        int size = argumentList.size();
        for (int i = 0; i < size; i++) {
            C0779b argument = argumentList.getArgument(i);
            C0779b argument2 = getArgument(argument.m190b());
            if (argument2 != null) {
                argument2.m191b(argument.m195f());
            }
        }
    }

    public void setReqArgs(ArgumentList argumentList) {
        int size = size();
        for (int i = 0; i < size; i++) {
            C0779b argument = getArgument(i);
            if (argument.m193d()) {
                String b = argument.m190b();
                C0779b argument2 = argumentList.getArgument(b);
                if (argument2 == null) {
                    throw new IllegalArgumentException("Argument \"" + b + "\" missing.");
                }
                argument.m191b(argument2.m195f());
            }
        }
    }

    public void setResArgs(ArgumentList argumentList) {
        int size = size();
        for (int i = 0; i < size; i++) {
            C0779b argument = getArgument(i);
            if (argument.m194e()) {
                String b = argument.m190b();
                C0779b argument2 = argumentList.getArgument(b);
                if (argument2 == null) {
                    throw new IllegalArgumentException("Argument \"" + b + "\" missing.");
                }
                argument.m191b(argument2.m195f());
            }
        }
    }
}
