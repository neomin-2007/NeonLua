local main = {
    name = "Chat",
    version = "1.0.0"
}

print("§a[Chat] Script de chat")
print("§a[Autor] Neomin")
print("§a[Versão] " .. main["version"])

local blocked_words = {
   [1] = "Batatinha",
   [2] = "Cebolinha",
   [3] = "Caralhinho",
   [4] = "Pipizinho"
}

local game_tags = {
    ["cargo.master"] = "§6[MASTER]",
    ["cargo.admin"] = "§c[ADMIN]",
    ["cargo.moderador"] = "§2[MOD]",
    ["cargo.ajudante"] = "§e[AJD]",
    ["cargo.vip_plus"] = "§a[VIP§e+§a]",
    ["cargo.vip"] = "§a[VIP]",
    ["cargo.default"] = "§f"
}

local function replace(str, pattern, replacement)
    if type(str) ~= "string" or type(pattern) ~= "string" then
        return str
    end
    
    replacement = replacement or ""
    local result, count = string.gsub(str, pattern, replacement)
    return result
end

local function onAsyncPlayerChat(event)
    local player = event:getPlayer()
    local message = event:getMessage()
    local server = Server()

    local tags = Tags()
    local current = event:getCurrentMillis()

    event.setCancelled(true)
    
    for _, word in pairs(blocked_words) do
        if message:lower():find(word:lower()) then
            player:sendMessage("§cMensagem contém linguagem imprópria!")
            break
        end
    end

    local player_prefix = "§f";
    for permission, prefix in ipairs(game_tags) do
        if player:hasPermission(permission) then
            player_prefix = prefix;
            break
        end
    end

    if tags.hasTag(player:getName(), "CHAT_COOLDOWN") then
        local cooldown = tags.getTag(player:getName(), "CHAT_COOLDOWN")
        if  cooldown >= current then
            local remain = tonumber(cooldown - current)/1000
            player:sendMessage("§cVocê precisa aguardar " .. remain .. " segundos para falar novamente no chat!")
            event.setCancelled(true)
            return
        end
    end

    tags.putTag(player:getName(), "CHAT_COOLDOWN", tonumber(current) + 5000)
    server:broadcastMessage("§e[l] " .. player_prefix .. " " .. player:getName() .. ": §e" .. message)
end

local function onCommandPreProcess(event)
    local player = event:getPlayer()
    local message = event:getMessage()
    local server = Server()

    local tags = Tags()
    local current = event:getCurrentMillis()

    if message:sub(1, 3) == "/c " or message:sub(1, 3) == "/l " then
        local chat_type = message:sub(1, 2)
        local chat_message = message:sub(4)
        
        local chat_prefix, chat_color
        if chat_type == "/c" then
            chat_prefix = "§7[c]"
            chat_color = "§7"
        else
            chat_prefix = "§e[l]"
            chat_color = "§e"
        end

        for _, word in pairs(blocked_words) do
            if chat_message:lower():find(word:lower()) then
                player:sendMessage("§cMensagem contém linguagem imprópria!")
                event.setCancelled(true)
                return
            end
        end

        if tags.hasTag(player:getName(), "CHAT_COOLDOWN") then
            local cooldown = tags.getTag(player:getName(), "CHAT_COOLDOWN")
            if  cooldown >= current then
                local remain = tonumber(cooldown - current)/1000
                player:sendMessage("§cVocê precisa aguardar " .. remain .. " segundos para falar novamente no chat!")
                event.setCancelled(true)
                return
            end
        end

        if player:isOp() or player:hasPermission("chat.color") then
            if (chat_type == "/c") then
                chat_message = replace(chat_message, "&", "§")
            end
        end

        local player_prefix = "§f";
        for permission, prefix in ipairs(game_tags) do
            if player:hasPermission(permission) then
                player_prefix = prefix;
                break
            end
        end

        tags.putTag(player:getName(), "CHAT_COOLDOWN", tonumber(current) + 5000)
        server:broadcastMessage(chat_prefix .. player_prefix .. " " .. player:getName() .. ": " .. chat_color .. chat_message)
        event.setCancelled(true)
        return
    end
end

return {
    main = main,
    onAsyncPlayerChat = onAsyncPlayerChat,
    onCommandPreProcess = onCommandPreProcess
}
