local main = {
    name = "CustomItems",
    version = "1.0.0"
}

print("§a[Chat] Script de CustomItems")
print("§a[Autor] Neomin")
print("§a[Versão] " .. main["version"])

local function getMidasSword(price_paid)

    bonus = 0

    if price_paid < 1000000 then
        bonus = price_paid / 50000
    elseif price_paid >= 1000000 and price_paid < 2500000 then
        bonus = (price_paid - 1000000) / 100000 + 20
    elseif price_paid >= 2500000 and price_paid < 7500000 then
        bonus = (price_paid - 2500000) / 200000 + 35
    elseif price_paid >= 7500000 and price_paid < 25000000 then
        bonus = (price_paid - 7500000) / 500000 + 60
    elseif price_paid >= 25000000 and price_paid < 50000000 then
        bonus = (price_paid - 25000000) / 1000000 + 95
    else
        bonus = 120
    end

    local item = itemBuilder("GOLD_SWORD", 1, 0)
    item:setDisplayName("§6Midas' Sword")
    item:addLoreLine("§7Damage: §c+" .. math.floor(170 + bonus))
    item:addLoreLine("")
    item:addLoreLine("§6Ability: Greed")
    item:addLoreLine("§7The strength and damage bonus")
    item:addLoreLine("§7of this item is dependent on the price")
    item:addLoreLine("§7paid for it at the §5Dark Auction§7!")
    item:addLoreLine("§7The maximum bonus of this item is §c120")
    item:addLoreLine("§7if the bid was §650,000,000 Coins §7for")
    item:addLoreLine("§7higher!")
    item:addLoreLine("")
    item:addLoreLine("§7Price paid: §6" .. format(price_paid) .. " §6Coins")
    item:addLoreLine("§7Strength Bonus: §c" .. bonus)
    item:addLoreLine("§7Damage Bonus: §c" .. bonus)
    item:addLoreLine("")
    item:addLoreLine("§8This item can be reforged!")
    item:addLoreLine("§cRequires §aCombat Skill 20")
    item:addLoreLine("§6§lLEGENDARY SWORD")
    item:setHideAllFlags(true)
    return item:getItem()
end

local function getAspectOfTheEnd()
    local item = itemBuilder("DIAMOND_SWORD", 1, 0)
    item:setDisplayName("§9Aspect of The End")
    item:addLoreLine("§7Damage: §c+100")
    item:addLoreLine("§7Strength: §c+100")
    item:addLoreLine("")
    item:addLoreLine("§6Ability: Instant Transmission §e§lRIGHT CLICK")
    item:addLoreLine("§7Teleport §a8 blocks §7ahead of you and")
    item:addLoreLine("§7gain §a+50 §f✦ Speed §7for §a3 seconds§7.")
    item:addLoreLine("§8Mana Cost: §350")
    item:addLoreLine("")
    item:addLoreLine("§8This item can be reforged!")
    item:addLoreLine("§9§lRARE SWORD")
    item:setHideAllFlags(true)
    return item:getItem()
end

local function getGiantsSword()
    local item = itemBuilder("IRON_SWORD", 1, 0)
    item:setDisplayName("§6Giant's Sword")
    item:addLoreLine("§7Gear Score: §d600 §8(650)")
    item:addLoreLine("§7Damage: §c+500 §8+(550)")
    item:addLoreLine("§7Swing Range: §a+1 §8(+1)")
    item:addLoreLine("")
    item:addLoreLine("§6Ability: Giant's Slam §e§lRIGHT CLICK")
    item:addLoreLine("§7Slam your sword into the ground dealing")
    item:addLoreLine("§c100,000 §7damage to nearby enemies.")
    item:addLoreLine("§8Mana Cost: §3100")
    item:addLoreLine("§8Cooldown: §a30s")
    item:addLoreLine("")
    item:addLoreLine("§8This item can be reforged!")
    item:addLoreLine("§cRequires The Catacombs Floor VI Completation.")
    item:addLoreLine("§6§lLEGENDARY DUNGEON SWORD")
    item:setHideAllFlags(true)
    return item:getItem()
end

local function onCommandPreProcess(event)
    local player = event:getPlayer()
    local message = event:getMessage()

    local inventory = player:getInventory()

    if string.sub(message, 1, 12) == "/customitems" then

    event.setCancelled(true)

    if message == "/customitems midasSword" then
        player:sendMessage("§7Gived §f1x §6Midas' Sword§7!")
        inventory:setItem(0, getMidasSword(46000000))
    end

    if message == "/customitems giantsSword" then
        player:sendMessage("§7Gived §f1x §6Giant's Sword§7!")
        inventory:setItem(1, getGiantsSword())
    end

    if message == "/customitems aote" then
        player:sendMessage("§7Gived §f1x §9Aspect of The End§7!")
        inventory:setItem(2, getAspectOfTheEnd())
    end

    end

end

function format(numero)
    if math.floor(numero) == numero then
        local str = tostring(numero)
        local resultado = ""
        local contador = 0
        
        for i = #str, 1, -1 do
            contador = contador + 1
            resultado = str:sub(i, i) .. resultado
            if contador % 3 == 0 and i ~= 1 then
                resultado = "," .. resultado
            end
        end
        
        return resultado
    else
        local parteInteira, parteDecimal = tostring(numero):match("^(%d+)%.(%d+)$")
        if not parteInteira then return tostring(numero) end  -- Caso o padrão não corresponda
        local parteInteiraFormatada = format(tonumber(parteInteira))
        
        parteDecimal = parteDecimal:sub(1, 2)
        
        return parteInteiraFormatada .. "," .. parteDecimal
    end
end

return {
    main = main,
    onCommandPreProcess = onCommandPreProcess
}
