select ITEM_NM, ITEM_COUNT from
  (select ITEM_NO, ITEM_NM, STANDARD_PRICE, count(ITEM_NO) ITEM_COUNT from
    (select PURCHASE_NO, MEMBER_NO, ITEM_NO, ITEM_NM, STANDARD_PRICE from
      (select PURCHASE_NO, MEMBER_NO from PURCHASE.PURCHASE
      union
      select PURCHASE_NO, MEMBER_NO from PURCHASE2.PURCHASE
      where MEMBER_NO in
        (select MEMBER_NO from
          (select MEMBER_NO, PLAY_TIME from
            (select MEMBER_NO, PLAY_TIME from MEMBER.MEMBER
            union
            select MEMBER_NO, PLAY_TIME from MEMBER2.MEMBER)
          order by PLAY_TIME desc)
        where ROWNUM <= 100)
      ) left join
      (select PURCHASE_NO, ITEM_NO, ITEM_NM, STANDARD_PRICE from
        (select PURCHASE_NO, ITEM_NO from PURCHASE.PURCHASE_DTL
        union
        select PURCHASE_NO, ITEM_NO from PURCHASE2.PURCHASE_DTL)
      left join ITEM using (ITEM_NO))
      using (PURCHASE_NO)
      order by STANDARD_PRICE desc
    )
  group by ITEM_NO, ITEM_NM, STANDARD_PRICE
  order by STANDARD_PRICE desc)
where ROWNUM <= 100
order by ITEM_COUNT
