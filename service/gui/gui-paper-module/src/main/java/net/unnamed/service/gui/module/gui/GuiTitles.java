package net.unnamed.service.gui.module.gui;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public enum GuiTitles {
    KNOWLEDGE_LIST(MiniMessage.miniMessage().deserialize("<white>%padding_split_complex_neg_7%<font:astopia:gui>\uE001</font>%padding_split_complex_neg_175%<font:astopia:gui>\uE000</font>%padding_split_complex_neg_176%<font:astopia:gui>\uE002</font>"));

    Component component;
}
