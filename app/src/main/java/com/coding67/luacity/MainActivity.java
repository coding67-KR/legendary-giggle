package com.coding67.luacity;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.JsePlatform;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends Activity {
    private Globals globals;
    private LuaValue game;
    private TextView stats;
    private TextView event;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        globals = JsePlatform.standardGlobals();
        try (InputStream in = getAssets().open("main.lua")) {
            // LuaJ 3.0.1 exposes the Reader overload; InputStream is not accepted directly.
            try (InputStreamReader reader = new InputStreamReader(in)) {
                game = globals.load(reader, "main.lua", globals).call();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        buildUi();
        refresh();
    }

    private void buildUi() {
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(28, 28, 28, 28);
        root.setBackgroundColor(Color.rgb(247, 248, 252));

        TextView title = new TextView(this);
        title.setText("🏙️ Lua City");
        title.setTextSize(30);
        title.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        title.setTextColor(Color.rgb(35, 42, 65));
        title.setGravity(Gravity.CENTER_HORIZONTAL);
        root.addView(title, new LinearLayout.LayoutParams(-1, 70));

        stats = new TextView(this);
        stats.setTextSize(19);
        stats.setTextColor(Color.rgb(35, 42, 65));
        stats.setGravity(Gravity.CENTER);
        stats.setPadding(12, 16, 12, 16);
        root.addView(stats, new LinearLayout.LayoutParams(-1, 160));

        event = new TextView(this);
        event.setTextSize(16);
        event.setTextColor(Color.DKGRAY);
        event.setGravity(Gravity.CENTER);
        root.addView(event, new LinearLayout.LayoutParams(-1, 110));

        root.addView(button("🌾 농장 건설  (35G + 목재 5)", v -> call("build_farm")));
        root.addView(button("🏠 집 건설  (45G + 목재 8)", v -> call("build_house")));
        root.addView(button("⏩ 다음 턴", v -> call("next_turn")));

        TextView hint = new TextView(this);
        hint.setText("작은 도시를 키우고 30턴을 버텨보세요!");
        hint.setTextSize(14);
        hint.setGravity(Gravity.CENTER);
        hint.setPadding(0, 24, 0, 0);
        root.addView(hint, new LinearLayout.LayoutParams(-1, -2));

        setContentView(root);
    }

    private Button button(String text, View.OnClickListener listener) {
        Button b = new Button(this);
        b.setText(text);
        b.setTextSize(16);
        b.setOnClickListener(listener);
        return b;
    }

    private void call(String method) {
        game.get(method).call();
        refresh();
    }

    private void refresh() {
        stats.setText(game.get("to_text").call().tojstring());
        event.setText("📣 " + game.get("event").tojstring());
    }
}
