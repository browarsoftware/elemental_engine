function Conversation(dialogId, javaObj1, javaObj2)
	javaObj1:setOutput(javaObj1:getOutput() + 1)
	javaObj2:setOutput(javaObj2:getOutput() + 2)
  if dialogId == 0 then
    return {{'Cześć! To mój pierwszy dialog'},{'Wow!','1'},{'Super! Są nawet polskie literki! Ąśćżó','2'},{'Eeeee - spodziewałem się czegoś lepszego...','3'}}
  elseif dialogId == 1 then
    return {{'Wiadomo, że WOW :-)','-1'}}
  elseif dialogId == 2 then
    return {{'Co to za polskie znaczki?'}, {'A takie tam', '100'}}
	elseif dialogId == 3 then
    return {{'A czego się spodziewałeś?'}, {'Rozbudowanych wątków, opowieści itp...', '4'},{'Sama nie wie... jakieś to takie brzydkie','5'}}
  else
    return {{'Hmmmm. To koniec rozmowy','-1'}}
  end
end

function SecondFunction(dialogId)
end

--[[t = Conversation(1)
print(t[1][1])]]
