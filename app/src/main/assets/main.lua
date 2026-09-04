math.randomseed(os.time())

local M = { money = 120, population = 18, food = 45, wood = 20, turn = 1, happiness = 72, event = '맑은 날씨입니다.' }

local events = {
  {'풍년!', 12, 4, 0},
  {'목재 상인이 방문했습니다.', 0, -2, 5},
  {'시장 축제가 열렸습니다.', 8, -3, 0},
  {'비가 오래 내려 식량이 줄었습니다.', -6, 7, 0},
  {'새 이주민이 왔습니다.', 2, -1, 0},
}

function M.next_turn()
  M.turn = M.turn + 1
  M.food = math.max(0, M.food - math.max(1, math.floor(M.population / 8)))
  M.money = M.money + math.max(3, math.floor(M.population * 0.55))
  if M.food > M.population then M.happiness = math.min(100, M.happiness + 1) else M.happiness = math.max(0, M.happiness - 4) end
  local e = events[math.random(#events)]
  M.event = e[1]
  M.happiness = math.max(0, math.min(100, M.happiness + e[2]))
  M.food = math.max(0, M.food + e[3])
  M.wood = math.max(0, M.wood + e[4])
end

function M.build_farm()
  if M.money >= 35 and M.wood >= 5 then
    M.money = M.money - 35; M.wood = M.wood - 5; M.food = M.food + 18; M.happiness = math.min(100, M.happiness + 2)
    M.event = '농장을 건설해 식량 생산이 늘었습니다.'
  else M.event = '자원이 부족합니다.' end
end

function M.build_house()
  if M.money >= 45 and M.wood >= 8 then
    M.money = M.money - 45; M.wood = M.wood - 8; M.population = M.population + 3; M.happiness = math.min(100, M.happiness + 3)
    M.event = '새 집이 완성되어 주민 3명이 이주했습니다.'
  else M.event = '자원이 부족합니다.' end
end

function M.to_text()
  return string.format('턴 %d|💰 %d G|👥 %d명|🌾 %d|🪵 %d|😊 %d%%|%s', M.turn, M.money, M.population, M.food, M.wood, M.happiness, M.event)
end

return M
