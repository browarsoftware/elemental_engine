/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Engine.Lua;

import Engine.PlayerStats;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleBindings;
import org.apache.commons.io.IOUtils;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.luaj.vm2.lib.jse.CoerceLuaToJava;
import org.luaj.vm2.lib.jse.JsePlatform;

/**
 *
 * @author Tomus
 */
public class PlayerLua {
    Bindings sb = null;
    public PlayerLua(String luaFile)
    {
        ScriptEngineManager sem = new ScriptEngineManager();
        ScriptEngine scriptEngine = sem.getEngineByName("luaj");
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream is = classloader.getResourceAsStream("Scripts/" +  luaFile);
        InputStreamReader isr = new InputStreamReader(is);
        CompiledScript script;
        try {
            script = ((Compilable) scriptEngine).compile(isr);
            isr.close();
            is.close();
            sb = new SimpleBindings();
            script.eval(sb); // Put the Lua functions into the sb environment
        } catch (ScriptException ex) {
            Logger.getLogger(PlayerLua.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PlayerLua.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Sentence[] DoConversation(int conversationId, PlayerStats[] ps)
    {
        LuaValue luaps1 = CoerceJavaToLua.coerce(ps[0]); // Java to Lua
        LuaValue luaps2 = CoerceJavaToLua.coerce(ps[1]); // Java to Lua
        //CoerceLuaToJava()
        LuaFunction MyAdd = (LuaFunction) sb.get("Conversation"); // Get Lua function
        LuaValue retvals = MyAdd.call(LuaValue.valueOf(conversationId), luaps1, luaps2); // Call the function

        int tableLength = retvals.length();
        Sentence[] sentence = new Sentence[tableLength];
        for (int a = 1; a <= tableLength; a++)
        {
            LuaValue retvals1 = retvals.get(a);
            sentence[a-1] = new Sentence();
            for (int b = 1; b <= retvals1.length(); b++)
            {
                String ss = retvals1.rawget(b).toString();
                if (b == 1)
                    sentence[a-1].setSentence(ss);
                if (b == 2)
                    sentence[a-1].setValue(Integer.parseInt(ss));
            }
            //sentence[a] =  
        }
        ps[0] = (PlayerStats)CoerceLuaToJava.coerce(luaps1, PlayerStats.class);
        ps[1] = (PlayerStats)CoerceLuaToJava.coerce(luaps2, PlayerStats.class);
        return sentence;
            /*LuaValue[] dogs = { luaDog };
            Varargs dist = onWalk.invoke(LuaValue.varargsOf(dogs)); // Alternative

            
            System.out.println("onWalk returned: " + dist);
            
            Dog retunredDog = (Dog)CoerceLuaToJava.coerce(luaDog, Dog.class);
            System.out.println("AAAAAAAAAa:" + retunredDog.name);*/

    }
    /*
    Globals _G = null;
    LuaValue chunk = null;
    public PlayerLua(String luaFile)
    {
        _G = JsePlatform.standardGlobals();
        _G.get("dofile").call( LuaValue.valueOf("Scripts/" + luaFile));

    }
    public Sentence[] DoConversation(int conversationId)
    {
        LuaValue MyAdd = _G.get("Conversation");
        //LuaValue MyAdd = chunk.metget("Conversation");
        LuaValue retvals = MyAdd.call(LuaValue.valueOf(conversationId));
        //LuaValue retvals = chunk.in("Conversation", LuaValue.valueOf(conversationId));
        int tableLength = retvals.length();

        Sentence[] sentence = new Sentence[tableLength];
        for (int a = 1; a <= tableLength; a++)
        {
            LuaValue retvals1 = retvals.get(a);
            sentence[a-1] = new Sentence();
            for (int b = 1; b <= retvals1.length(); b++)
            {
                String ss = retvals1.rawget(b).toString();
                if (b == 1)
                    sentence[a-1].setSentence(ss);
                if (b == 2)
                    sentence[a-1].setValue(Integer.parseInt(ss));
            }
            //sentence[a] =  
        }
        return sentence;
    }
    */
    /*
    public static void main(String[]args)
    {

        //PlayerLua c1 = new PlayerLua("e:\\Projects\\java 3d tutorial\\Game\\src\\Scripts\\PlayerTemplate.lua");
        PlayerLua c1 = new PlayerLua("PlayerTemplate.lua");
        DialogJDialog df = new DialogJDialog(c1, null, "bleble", true);
        df.setVisible(true);
        df.dispose();
    }*/
   
    
    class Dog
    {
      public String name;
      Dog(String n) { name = n; }
      public void SetName(String name) { this.name = name; }
      public void talk() { System.out.println("Dog " + name + " barks!"); }
      public void walk() { System.out.println("Dog " + name + " walks..."); }
    }
    class Cat
    {
      public String name;
      Cat(String n) { name = n; }
      public void SetName(String name) { this.name = name; }
      public void talk() { System.out.println("Cat " + name + " meows!"); }
      public void walk() { System.out.println("Cat " + name + " walks..."); }
    }
    
    
    public void tester(String luaFile)
    {
        Dog dog = new Dog("Rex");
        Cat cat = new Cat("Felix");
        ScriptEngineManager sem = new ScriptEngineManager();
        ScriptEngine scriptEngine = sem.getEngineByName("luaj");
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream is = classloader.getResourceAsStream("Scripts/" +  luaFile);
        InputStreamReader isr = new InputStreamReader(is);
        CompiledScript script;
        try {
            script = ((Compilable) scriptEngine).compile(isr);
            isr.close();
            is.close();
            Bindings sb = new SimpleBindings();
            script.eval(sb); // Put the Lua functions into the sb environment
            LuaValue luaDog = CoerceJavaToLua.coerce(dog); // Java to Lua
            //CoerceLuaToJava()
            LuaFunction onTalk = (LuaFunction) sb.get("onTalk"); // Get Lua function
            LuaValue b = onTalk.call(luaDog); // Call the function
            System.out.println("onTalk answered: " + b);
            LuaFunction onWalk = (LuaFunction) sb.get("onWalk");
            LuaValue[] dogs = { luaDog };
            Varargs dist = onWalk.invoke(LuaValue.varargsOf(dogs)); // Alternative

            
            System.out.println("onWalk returned: " + dist);
            
            Dog retunredDog = (Dog)CoerceLuaToJava.coerce(luaDog, Dog.class);
            System.out.println("AAAAAAAAAa:" + retunredDog.name);
        } catch (ScriptException ex) {
            Logger.getLogger(PlayerLua.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PlayerLua.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    /*
    public static void main(String[]args)
    {
        PlayerLua pl = new PlayerLua("test_object.lua");
        pl.tester("test_object.lua");
    }
    */
    private static String ReadStringFile(String path)
    {
        String luaFile = null;
        try {
            luaFile = new String(Files.readAllBytes(Paths.get(path)));
        } catch (IOException ex) {
            Logger.getLogger(PlayerLua.class.getName()).log(Level.SEVERE, null, ex);
        }
        return luaFile;
    }
}
