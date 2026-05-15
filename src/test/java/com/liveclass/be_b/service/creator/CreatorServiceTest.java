package com.liveclass.be_b.service.creator;

import com.liveclass.be_b.common.exception.BusinessException;
import com.liveclass.be_b.common.exception.ErrorCode;
import com.liveclass.be_b.domain.creator.entity.Creator;
import com.liveclass.be_b.repository.creator.CreatorRepository;
import com.liveclass.be_b.support.TestFixtureFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreatorServiceTest {

    @Mock
    private CreatorRepository creatorRepository;

    @InjectMocks
    private CreatorService creatorService;

    @Test
    @DisplayName("크리에이터를 조회할 수 있다")
    void findCreator() {
        Creator creator = TestFixtureFactory.creator();
        when(creatorRepository.findById("creator-1")).thenReturn(Optional.of(creator));

        Creator result = creatorService.findCreator("creator-1");

        assertThat(result).isSameAs(creator);
    }

    @Test
    @DisplayName("크리에이터가 없으면 예외가 발생한다")
    void findCreatorNotFound() {
        when(creatorRepository.findById("creator-1")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> creatorService.findCreator("creator-1"))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.NOT_FOUND_CREATOR);
    }

    @Test
    @DisplayName("전체 크리에이터를 조회할 수 있다")
    void findAllCreators() {
        List<Creator> creators = List.of(
                TestFixtureFactory.creator("creator-1", "creator-1", "김강사"),
                TestFixtureFactory.creator("creator-2", "creator-2", "이강사")
        );
        when(creatorRepository.findAll()).thenReturn(creators);

        List<Creator> result = creatorService.findAllCreators();

        assertThat(result).containsExactlyElementsOf(creators);
    }
}
