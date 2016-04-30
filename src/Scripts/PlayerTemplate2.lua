function Conversation(dialogId)
  if dialogId == 0 then
    return {{'Cześć! To mój pierwszy dialog2',"-1"},{'Wow!','1'},{'Super! Ąśćżó','2'},{'Eeeee','3'}}
  elseif dialogId == 1 then
    return {{'Wiadomo, że WOW :-)','-1'}}
  elseif dialogId == 2 then
    return {{'Co to za polskie znaczki?','-1'}, {'A takie tam', '4'}}
  else
    return {{'Hmmmm. To koniec rozmowy','-1'}}
  end
end

--[[t = Conversation(1)
print(t[1][1])]]
